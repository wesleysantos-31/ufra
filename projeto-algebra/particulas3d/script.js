// --- CONFIGURAÇÃO GLOBAL ---
const PARTICLE_COUNT = 25000;
const EXPLOSION_RANGE = 40;
let isDebugVisible = false;

// --- VARIÁVEIS DE ESTADO ---
const state = {
    shape: 'sphere',
    baseZoom: 20,
    handZoom: 0,
    currentZoom: 20,
    rotationVelocity: { x: 0, y: 0 },
    autoRotate: true,
    isHandClosed: false,
    handPos: { x: 0, y: 0 }, // Normalizado -1 a 1
    prevHandPos: { x: 0, y: 0 },
    explosionFactor: 0, // 0 = unido, 1 = explodido
    color: new THREE.Color('#00f2ff')
};

// --- THREE.JS SETUP ---
const container = document.getElementById('canvas-container');
const scene = new THREE.Scene();
// Leve neblina para profundidade
scene.fog = new THREE.FogExp2(0x050505, 0.02);

const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
camera.position.z = 20;

const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.setPixelRatio(window.devicePixelRatio);
container.appendChild(renderer.domElement);

// --- SISTEMA DE PARTÍCULAS ---
const geometry = new THREE.BufferGeometry();
const positions = new Float32Array(PARTICLE_COUNT * 3);
const targetPositions = new Float32Array(PARTICLE_COUNT * 3);
const randomPositions = new Float32Array(PARTICLE_COUNT * 3); // Para explosão

// Inicializar posições aleatórias
for (let i = 0; i < PARTICLE_COUNT * 3; i++) {
    positions[i] = (Math.random() - 0.5) * 100;
    randomPositions[i] = (Math.random() - 0.5) * EXPLOSION_RANGE * 2;
}

geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));

// Textura suave para partículas
const sprite = new THREE.TextureLoader().load('https://threejs.org/examples/textures/sprites/disc.png');

const material = new THREE.PointsMaterial({
    color: state.color,
    size: 0.3,
    sizeAttenuation: true,
    map: sprite,
    alphaTest: 0.5,
    transparent: true,
    opacity: 0.9,
    blending: THREE.AdditiveBlending
});

const particles = new THREE.Points(geometry, material);
scene.add(particles);

// --- GERADORES DE FORMAS ---

function setTargetShape(type) {
    // Atualiza botões
    document.querySelectorAll('.shape-grid button').forEach(b => b.classList.remove('active'));
    const btn = document.getElementById('btn-' + type) || document.getElementById('btn-text');
    if(btn) btn.classList.add('active');

    let idx = 0;
    const tempVec = new THREE.Vector3();

    // Lógica de geração de formas
    if (type === 'sphere') {
        const radius = 6;
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            const phi = Math.acos(-1 + (2 * i) / PARTICLE_COUNT);
            const theta = Math.sqrt(PARTICLE_COUNT * Math.PI) * phi;
            targetPositions[idx++] = radius * Math.cos(theta) * Math.sin(phi);
            targetPositions[idx++] = radius * Math.sin(theta) * Math.sin(phi);
            targetPositions[idx++] = radius * Math.cos(phi);
        }
    } else if (type === 'cube') {
            const size = 8;
            for (let i = 0; i < PARTICLE_COUNT; i++) {
            targetPositions[idx++] = (Math.random() - 0.5) * size;
            targetPositions[idx++] = (Math.random() - 0.5) * size;
            targetPositions[idx++] = (Math.random() - 0.5) * size;
            }
    } else if (type === 'heart') {
        // Fórmulas paramétricas para coração 3D
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            const t = Math.random() * Math.PI * 2;
            const u = Math.random() * Math.PI; // Distribuição não uniforme mas visualmente ok
            
            // Escala base
            const s = 0.35;
            
            // Variação de uma fórmula comum de coração
            const x = 16 * Math.pow(Math.sin(t), 3);
            const y = 13 * Math.cos(t) - 5 * Math.cos(2*t) - 2 * Math.cos(3*t) - Math.cos(4*t);
            const z = (Math.random() - 0.5) * 6; // Espessura

            targetPositions[idx++] = x * s;
            targetPositions[idx++] = y * s;
            targetPositions[idx++] = z;
        }
    } else if (type === 'saturn') {
        const planetRadius = 4;
        const ringInner = 5.5;
        const ringOuter = 9;
        
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            if (i < PARTICLE_COUNT * 0.4) {
                // Planeta
                const u = Math.random();
                const v = Math.random();
                const theta = 2 * Math.PI * u;
                const phi = Math.acos(2 * v - 1);
                targetPositions[idx++] = planetRadius * Math.sin(phi) * Math.cos(theta);
                targetPositions[idx++] = planetRadius * Math.sin(phi) * Math.sin(theta);
                targetPositions[idx++] = planetRadius * Math.cos(phi);
            } else {
                // Anéis
                const angle = Math.random() * Math.PI * 2;
                const dist = ringInner + Math.random() * (ringOuter - ringInner);
                targetPositions[idx++] = Math.cos(angle) * dist;
                targetPositions[idx++] = (Math.random() - 0.5) * 0.2; // Espessura fina
                targetPositions[idx++] = Math.sin(angle) * dist;
            }
        }
    } else if (type === 'flower') {
            for (let i = 0; i < PARTICLE_COUNT; i++) {
            const u = Math.random() * Math.PI * 2;
            const v = Math.random() * Math.PI; 
            const r = 5 + Math.cos(5 * u) * Math.sin(v) * 3; // Pétalas
            
            targetPositions[idx++] = r * Math.sin(v) * Math.cos(u);
            targetPositions[idx++] = r * Math.sin(v) * Math.sin(u);
            targetPositions[idx++] = r * Math.cos(v);
            }
    } else if (type === 'buddha') {
        // Abstração de uma figura meditando (Cabeça + Torso + Pernas)
        // Usando composição de esferas e ovais
        for(let i=0; i<PARTICLE_COUNT; i++) {
            const r = Math.random();
            let px, py, pz;
            
            if(r < 0.20) { // Cabeça
                const u = Math.random() * Math.PI * 2;
                const v = Math.random() * Math.PI;
                const rad = 1.8;
                px = rad * Math.sin(v) * Math.cos(u);
                py = rad * Math.sin(v) * Math.sin(u) + 3.5; // Offset Y
                pz = rad * Math.cos(v);
            } else if (r < 0.60) { // Torso
                const u = Math.random() * Math.PI * 2;
                const h = (Math.random() - 0.5) * 5;
                const rad = 2.5 * (1 - Math.abs(h)/6); // Tapering
                px = rad * Math.cos(u);
                py = h; 
                pz = rad * Math.sin(u);
            } else { // Pernas/Base (Disco achatado e largo)
                const angle = Math.random() * Math.PI * 2;
                const rad = 2 + Math.random() * 4;
                px = rad * Math.cos(angle);
                py = -2.5 + (Math.random() * 1.5);
                pz = rad * Math.sin(angle);
            }
            targetPositions[idx++] = px;
            targetPositions[idx++] = py;
            targetPositions[idx++] = pz;
        }
    }
}

// --- GERADOR DE TEXTO EM PARTÍCULAS ---
function setTextShape() {
    document.querySelectorAll('.shape-grid button').forEach(b => b.classList.remove('active'));
    document.getElementById('btn-text').classList.add('active');

    const textCanvas = document.createElement('canvas');
    const ctx = textCanvas.getContext('2d');
    textCanvas.width = 400;
    textCanvas.height = 200;
    
    ctx.fillStyle = 'black';
    ctx.fillRect(0,0, 400, 200);
    ctx.fillStyle = 'white';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    
    // Renderiza Texto
    ctx.font = 'bold 50px Arial';
    ctx.fillText("Álgebra", 200, 70);
    ctx.font = 'bold 55px Arial Black';
    ctx.fillStyle = '#ccc';
    ctx.fillText("Linear", 200, 140);

    const imageData = ctx.getImageData(0, 0, 400, 200);
    const data = imageData.data;
    
    // Encontrar pixels válidos
    const validPixels = [];
    for(let y=0; y<200; y+=2) { // Step 2 para performance
        for(let x=0; x<400; x+=2) {
            const i = (y * 400 + x) * 4;
            if(data[i] > 50) { // Se pixel for brilhante
                validPixels.push({
                    x: (x - 200) / 15, // Centralizar e escalar
                    y: -(y - 100) / 15
                });
            }
        }
    }

    // Distribuir partículas nos pixels
    let idx = 0;
    for(let i=0; i<PARTICLE_COUNT; i++) {
        // Se houver mais partículas que pixels, reusar pixels aleatoriamente
        const p = validPixels[Math.floor(Math.random() * validPixels.length)];
        if(p) {
            targetPositions[idx++] = p.x;
            targetPositions[idx++] = p.y;
            targetPositions[idx++] = (Math.random() - 0.5) * 1; // Leve profundidade
        } else {
            // Sobra para o infinito
            targetPositions[idx++] = (Math.random()-0.5)*100;
            targetPositions[idx++] = (Math.random()-0.5)*100;
            targetPositions[idx++] = (Math.random()-0.5)*100;
        }
    }
    
    // Ajustar zoom automaticamente para caber
    state.baseZoom = 15;
    document.getElementById('zoomSlider').value = 15;
}

// --- CONTROLES DA UI ---
function setShape(type) {
    state.shape = type;
    setTargetShape(type);
}

document.getElementById('zoomSlider').addEventListener('input', (e) => {
    state.baseZoom = parseFloat(e.target.value);
});

document.getElementById('colorPicker').addEventListener('input', (e) => {
    state.color.set(e.target.value);
    material.color = state.color;
});

function toggleDebug() {
    const panel = document.getElementById('debug-panel');
    isDebugVisible = !isDebugVisible;
    panel.style.display = isDebugVisible ? 'block' : 'none';
}

// --- MEDIAPIPE HANDS SETUP ---
const videoElement = document.getElementById('debug-video');
const canvasElement = document.getElementById('debug-canvas');
const canvasCtx = canvasElement.getContext('2d');

function onResults(results) {
    document.getElementById('loader').style.display = 'none';

    // Desenhar debug se visível
    if (isDebugVisible) {
        canvasCtx.save();
        canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height);
        canvasCtx.drawImage(results.image, 0, 0, canvasElement.width, canvasElement.height);
        if (results.multiHandLandmarks) {
            for (const landmarks of results.multiHandLandmarks) {
                drawConnectors(canvasCtx, landmarks, HAND_CONNECTIONS, {color: '#00FF00', lineWidth: 2});
                drawLandmarks(canvasCtx, landmarks, {color: '#FF0000', lineWidth: 1});
            }
        }
        canvasCtx.restore();
    }

    if (results.multiHandLandmarks && results.multiHandLandmarks.length > 0) {
        const landmarks = results.multiHandLandmarks[0];
        
        // 1. Detectar se mão está aberta ou fechada (Fist detection)
        // CORREÇÃO: Usar proporção relativa em vez de distância absoluta para suportar Z-depth
        const wrist = landmarks[0];
        const middleBase = landmarks[9]; // Ponto de referência para escala
        const middleTip = landmarks[12];

        // Tamanho da mão na tela (usado para normalizar a distância)
        const handScale = Math.hypot(middleBase.x - wrist.x, middleBase.y - wrist.y);
        
        // Distância da ponta ao pulso
        const tipDistance = Math.hypot(middleTip.x - wrist.x, middleTip.y - wrist.y);
        
        // Razão de abertura independente da distância da câmera
        // Geralmente: > 1.6 é aberta, < 1.2 é fechada
        const openRatio = tipDistance / handScale;
        
        // Limiar calibrado relativo
        const isClosed = openRatio < 1.3; 
        
        state.isHandClosed = isClosed;

        // Alvo da explosão: Se fechado (0), forma unida. Se aberto (1), explode.
        // Mas a lógica do usuário é: Aberto = Explosão. Fechado = Unir.
        // Logo: isClosed=true -> factor=0. isClosed=false -> factor=1.
        // Usamos Lerp no loop de animação para suavizar.

        // 2. Rotação (Inércia)
        // Espelhar movimento: x invertido
        const currentX = (1 - landmarks[9].x) * 2 - 1; // Centralizado -1 a 1
        const currentY = -(landmarks[9].y * 2 - 1);    // Centralizado -1 a 1

        if (isClosed) {
            // Mão fechada controla rotação
            state.autoRotate = false;
            const deltaX = currentX - state.prevHandPos.x;
            const deltaY = currentY - state.prevHandPos.y;

            // Sensibilidade
            state.rotationVelocity.x = deltaX * 3;
            state.rotationVelocity.y = deltaY * 3;
        } else {
            // Mão aberta, permite que a inércia continue ou auto-rotação leve
            // Não para imediatamente
        }

        state.prevHandPos.x = currentX;
        state.prevHandPos.y = currentY;

        // 3. Zoom (Distância Z estimada)
        // Baseado na escala da mão (distância pulso -> base do indicador)
        const indexBase = landmarks[5];
        const scaleDist = Math.hypot(indexBase.x - wrist.x, indexBase.y - wrist.y);
        
        // Quanto maior a mão (mais perto), scaleDist aumenta.
        // Normalizar: 0.1 (longe) a 0.5 (muito perto)
        // Mapear para Zoom offset: Longe = +ZoomOut, Perto = -ZoomIn
        const zoomFactor = (scaleDist - 0.2) * 80; // Multiplicador arbitrário
        
        // Suavização do Zoom
        state.handZoom += ((-zoomFactor) - state.handZoom) * 0.1;

    } else {
        // Sem mão
        state.isHandClosed = false;
        state.autoRotate = true;
        state.handZoom += (0 - state.handZoom) * 0.1; // Reset zoom da mão
    }
}

const hands = new Hands({locateFile: (file) => {
    return `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}`;
}});

hands.setOptions({
    maxNumHands: 1,
    modelComplexity: 1,
    minDetectionConfidence: 0.5,
    minTrackingConfidence: 0.5
});

hands.onResults(onResults);

const cameraUtils = new Camera(videoElement, {
    onFrame: async () => {
        await hands.send({image: videoElement});
    },
    width: 320,
    height: 240
});

// Iniciar câmera
cameraUtils.start();


// --- LOOP DE ANIMAÇÃO PRINCIPAL ---
function animate() {
    requestAnimationFrame(animate);

    const posAttribute = geometry.attributes.position;
    const currentPositions = posAttribute.array;

    // 1. Gerenciar Estado de Explosão com Suavização (Lerp)
    // Se mão aberta (not closed), targetExplosion = 1. Se fechada, 0.
    const targetExplosion = state.isHandClosed ? 0 : 1;
    // Lerp simples: current = current + (target - current) * speed
    state.explosionFactor += (targetExplosion - state.explosionFactor) * 0.05;

    // 2. Física de Rotação (Inércia)
    particles.rotation.y += state.rotationVelocity.x;
    particles.rotation.x -= state.rotationVelocity.y;

    // Atrito (Friction) para parar devagar
    state.rotationVelocity.x *= 0.95;
    state.rotationVelocity.y *= 0.95;

    // Auto-rotação muito lenta se estiver parado e mão não detectada
    if(Math.abs(state.rotationVelocity.x) < 0.001 && state.autoRotate) {
        particles.rotation.y += 0.002;
    }

    // 3. Atualizar Partículas
    for (let i = 0; i < PARTICLE_COUNT; i++) {
        const ix = i * 3;
        const iy = i * 3 + 1;
        const iz = i * 3 + 2;

        // Posição Alvo (Forma)
        let tx = targetPositions[ix];
        let ty = targetPositions[iy];
        let tz = targetPositions[iz];

        // Posição de Explosão (Caos)
        // Mistura a posição alvo com um vetor de ruído baseado no fator de explosão
        const rx = randomPositions[ix];
        const ry = randomPositions[iy];
        const rz = randomPositions[iz];

        // Interpolação final da posição
        // Se explosionFactor for 0, vai para target. Se 1, vai para (target + random).
        // Adicionamos também um pouco de movimento senoidal para "vida" nas partículas
        const time = Date.now() * 0.001;
        const breathing = Math.sin(time + tx) * 0.05;

        const destX = tx + (rx * state.explosionFactor) + breathing;
        const destY = ty + (ry * state.explosionFactor) + breathing;
        const destZ = tz + (rz * state.explosionFactor);

        // Mover partícula suavemente em direção ao destino (Easing)
        currentPositions[ix] += (destX - currentPositions[ix]) * 0.08;
        currentPositions[iy] += (destY - currentPositions[iy]) * 0.08;
        currentPositions[iz] += (destZ - currentPositions[iz]) * 0.08;
    }

    posAttribute.needsUpdate = true;

    // 4. Gerenciar Zoom
    // Zoom Final = Slider Base + Zoom da Mão
    const targetZoom = state.baseZoom + state.handZoom;
    // Suavizar câmera
    state.currentZoom += (targetZoom - state.currentZoom) * 0.1;
    
    // Limites de segurança
    if(state.currentZoom < 1) state.currentZoom = 1;
    
    camera.position.z = state.currentZoom;

    renderer.render(scene, camera);
}

// Listener de redimensionamento
window.addEventListener('resize', () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});

// Setup inicial
setShape('sphere');
animate();