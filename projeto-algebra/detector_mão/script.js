// --- Elementos DOM ---
const videoElement = document.getElementsByClassName('input_video')[0];
const canvasElement = document.getElementsByClassName('output_canvas')[0];
const canvasCtx = canvasElement.getContext('2d');
const mainText = document.getElementById('main-text');
const confText = document.getElementById('conf-text');
const displayBox = document.getElementById('display-box');
const loader = document.getElementById('loader');

// --- Inicialização ---
async function init() {
    // Configura Câmera e MediaPipe
    const hands = new Hands({
        locateFile: (file) => {
            return `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}`;
        }
    });

    hands.setOptions({
        maxNumHands: 2, // Permite 2 mãos
        modelComplexity: 1,
        minDetectionConfidence: 0.7,
        minTrackingConfidence: 0.7
    });

    hands.onResults(onResults);

    const camera = new Camera(videoElement, {
        onFrame: async () => {
            await hands.send({ image: videoElement });
        },
        width: 1280,
        height: 720
    });

    await camera.start();
    loader.style.display = 'none';
}

// --- Loop Principal (Frame a Frame) ---
function onResults(results) {
    // Ajustar Canvas
    canvasElement.width = videoElement.videoWidth;
    canvasElement.height = videoElement.videoHeight;

    canvasCtx.save();
    canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height);

    // Desenhar Imagem da Câmera
    canvasCtx.drawImage(results.image, 0, 0, canvasElement.width, canvasElement.height);

    let totalFingers = 0;
    let handsDetected = false;

    if (results.multiHandLandmarks && results.multiHandLandmarks.length > 0) {
        handsDetected = true;

        // Itera sobre todas as mãos detectadas
        for (let i = 0; i < results.multiHandLandmarks.length; i++) {
            const landmarks = results.multiHandLandmarks[i];
            const handedness = results.multiHandedness[i].label; // 'Left' ou 'Right'

            // Desenhar Esqueleto
            drawConnectors(canvasCtx, landmarks, HAND_CONNECTIONS, { color: '#00ff88', lineWidth: 4 });
            drawLandmarks(canvasCtx, landmarks, { color: '#ffffff', lineWidth: 1, radius: 3 });

            // Somar dedos desta mão
            totalFingers += countFingers(landmarks, handedness);
        }

        // Atualiza Interface
        mainText.innerText = totalFingers;
        confText.innerText = `Dedos levantados (Total)`;

        displayBox.style.borderColor = "#00ff88";
        displayBox.style.boxShadow = "0 0 30px rgba(0, 255, 136, 0.4)";
        mainText.style.color = "#00ff88";

    } else {
        mainText.innerText = "-";
        confText.innerText = "Posicione as mãos";

        displayBox.style.borderColor = "#555";
        displayBox.style.boxShadow = "none";
        mainText.style.color = "#555";
    }
    canvasCtx.restore();
}

function countFingers(landmarks, handedness) {
    const tips = [8, 12, 16, 20]; // Indicador, Médio, Anelar, Mínimo
    const pips = [6, 10, 14, 18]; // Articulações inferiores correspondentes

    let count = 0;

    // 1. Dedos (Exceto Polegar)
    for (let i = 0; i < tips.length; i++) {
        if (landmarks[tips[i]].y < landmarks[pips[i]].y) {
            count++;
        }
    }

    // 2. Polegar
    const thumbTip = landmarks[4];
    const thumbIP = landmarks[3];
    const indexMCP = landmarks[5];
    const pinkyMCP = landmarks[17];

    let isThumbOpen = false;

    // Lógica baseada na orientação relativa da mão (Index vs Pinky)
    // Isso funciona independente de espelhamento, mão esquerda/direita ou palma/costas

    if (indexMCP.x < pinkyMCP.x) {
        // Orientação: Polegar deve estar à Esquerda (X menor)
        // Exemplo: Mão Direita (Palma) ou Mão Esquerda (Costas)
        if (thumbTip.x < thumbIP.x) {
            isThumbOpen = true;
        }
    } else {
        // Orientação: Polegar deve estar à Direita (X maior)
        // Exemplo: Mão Esquerda (Palma) ou Mão Direita (Costas)
        if (thumbTip.x > thumbIP.x) {
            isThumbOpen = true;
        }
    }

    if (isThumbOpen) {
        count++;
    }

    return count;
}

// Inicia o app
init();
