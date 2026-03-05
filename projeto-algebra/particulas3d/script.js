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
    isPointing: false,       // Novo: dedo indicador apontando
    pointerPos: { x: 0, y: 0, z: 0 }, // Posição 3D da ponta do indicador
    handPos: { x: 0, y: 0 },
    prevHandPos: { x: 0, y: 0 },
    explosionFactor: 0,      // 0 = unido, 1 = explodido
    color: new THREE.Color('#00f2ff')
};

// --- THREE.JS SETUP ---
const container = document.getElementById('canvas-container');
const scene = new THREE.Scene();
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
const randomPositions = new Float32Array(PARTICLE_COUNT * 3);

for (let i = 0; i < PARTICLE_COUNT * 3; i++) {
    positions[i] = (Math.random() - 0.5) * 100;
    randomPositions[i] = (Math.random() - 0.5) * EXPLOSION_RANGE * 2;
}

// Sistema de pó mágico: cada partícula tem sua própria velocidade (inércia)
const particleVelocities = new Float32Array(PARTICLE_COUNT * 3); // vx, vy, vz por partícula
// Fase única por partícula (para a ondulação senoidal individual)
const particlePhase = new Float32Array(PARTICLE_COUNT);
for (let i = 0; i < PARTICLE_COUNT; i++) {
    particlePhase[i] = Math.random() * Math.PI * 2;
}

geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));

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
    document.querySelectorAll('.shape-grid button').forEach(b => b.classList.remove('active'));
    const btn = document.getElementById('btn-' + type) || document.getElementById('btn-text');
    if (btn) btn.classList.add('active');

    let idx = 0;

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
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            const t = Math.random() * Math.PI * 2;
            const s = 0.35;
            const x = 16 * Math.pow(Math.sin(t), 3);
            const y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);
            const z = (Math.random() - 0.5) * 6;
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
                const u = Math.random();
                const v = Math.random();
                const theta = 2 * Math.PI * u;
                const phi = Math.acos(2 * v - 1);
                targetPositions[idx++] = planetRadius * Math.sin(phi) * Math.cos(theta);
                targetPositions[idx++] = planetRadius * Math.sin(phi) * Math.sin(theta);
                targetPositions[idx++] = planetRadius * Math.cos(phi);
            } else {
                const angle = Math.random() * Math.PI * 2;
                const dist = ringInner + Math.random() * (ringOuter - ringInner);
                targetPositions[idx++] = Math.cos(angle) * dist;
                targetPositions[idx++] = (Math.random() - 0.5) * 0.2;
                targetPositions[idx++] = Math.sin(angle) * dist;
            }
        }
    } else if (type === 'flower') {
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            const u = Math.random() * Math.PI * 2;
            const v = Math.random() * Math.PI;
            const r = 5 + Math.cos(5 * u) * Math.sin(v) * 3;
            targetPositions[idx++] = r * Math.sin(v) * Math.cos(u);
            targetPositions[idx++] = r * Math.sin(v) * Math.sin(u);
            targetPositions[idx++] = r * Math.cos(v);
        }
    } else if (type === 'buddha') {
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            const r = Math.random();
            let px, py, pz;
            if (r < 0.20) {
                const u = Math.random() * Math.PI * 2;
                const v = Math.random() * Math.PI;
                const rad = 1.8;
                px = rad * Math.sin(v) * Math.cos(u);
                py = rad * Math.sin(v) * Math.sin(u) + 3.5;
                pz = rad * Math.cos(v);
            } else if (r < 0.60) {
                const u = Math.random() * Math.PI * 2;
                const h = (Math.random() - 0.5) * 5;
                const rad = 2.5 * (1 - Math.abs(h) / 6);
                px = rad * Math.cos(u);
                py = h;
                pz = rad * Math.sin(u);
            } else {
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
    ctx.fillRect(0, 0, 400, 200);
    ctx.fillStyle = 'white';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    ctx.font = 'bold 50px Arial';
    ctx.fillText("Álgebra", 200, 70);
    ctx.font = 'bold 55px Arial Black';
    ctx.fillStyle = '#ccc';
    ctx.fillText("Linear", 200, 140);

    const imageData = ctx.getImageData(0, 0, 400, 200);
    const data = imageData.data;

    const validPixels = [];
    for (let y = 0; y < 200; y += 2) {
        for (let x = 0; x < 400; x += 2) {
            const i = (y * 400 + x) * 4;
            if (data[i] > 50) {
                validPixels.push({
                    x: (x - 200) / 15,
                    y: -(y - 100) / 15
                });
            }
        }
    }

    // Distribuir partículas nos pixels
    let idx = 0;
    for (let i = 0; i < PARTICLE_COUNT; i++) {
        const p = validPixels[Math.floor(Math.random() * validPixels.length)];
        if (p) {
            targetPositions[idx++] = p.x;
            targetPositions[idx++] = p.y;
            targetPositions[idx++] = (Math.random() - 0.5) * 1;
        } else {
            targetPositions[idx++] = (Math.random() - 0.5) * 100;
            targetPositions[idx++] = (Math.random() - 0.5) * 100;
            targetPositions[idx++] = (Math.random() - 0.5) * 100;
        }
    }

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

// Canvas overlay fullscreen para o esqueleto da mão
const handOverlay = document.getElementById('hand-overlay');
const overlayCtx = handOverlay.getContext('2d');

function resizeOverlay() {
    handOverlay.width = window.innerWidth;
    handOverlay.height = window.innerHeight;
}
resizeOverlay();
window.addEventListener('resize', resizeOverlay);

function onResults(results) {
    document.getElementById('loader').style.display = 'none';

    // Desenhar debug se visível
    if (isDebugVisible) {
        // Evita distorção ajustando as dimensões do canvas para a resolução real do vídeo
        if (canvasElement.width !== results.image.width) {
            canvasElement.width = results.image.width;
            canvasElement.height = results.image.height;
        }
        canvasCtx.save();
        canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height);
        canvasCtx.drawImage(results.image, 0, 0, canvasElement.width, canvasElement.height);
        if (results.multiHandLandmarks) {
            for (const landmarks of results.multiHandLandmarks) {
                drawConnectors(canvasCtx, landmarks, HAND_CONNECTIONS, { color: '#00FF00', lineWidth: 2 });
                drawLandmarks(canvasCtx, landmarks, { color: '#FF0000', lineWidth: 1 });
            }
        }
        canvasCtx.restore();
    }

    if (results.multiHandLandmarks && results.multiHandLandmarks.length > 0) {
        const landmarks = results.multiHandLandmarks[0];

        // 1. Detectar se mão está aberta ou fechada
        const wrist = landmarks[0];
        const middleBase = landmarks[9];
        const middleTip = landmarks[12];

        const handScale = Math.hypot(middleBase.x - wrist.x, middleBase.y - wrist.y);
        const tipDistance = Math.hypot(middleTip.x - wrist.x, middleTip.y - wrist.y);
        const openRatio = tipDistance / handScale;
        const isClosed = openRatio < 1.3;
        state.isHandClosed = isClosed;

        // 2. Detectar gesto de APONTAR (☝️ só o indicador esticado)
        // Lógica: se a ponta do dedo (tip) está ACIMA da articulação do meio (PIP),
        // o dedo está esticado. Em coordenadas MediaPipe, y=0 é o topo da tela.
        const fingerExtended = (tipIdx, pipIdx) => landmarks[tipIdx].y < landmarks[pipIdx].y;

        const indexExtended = fingerExtended(8, 6);  // indicador
        const middleExtended = fingerExtended(12, 10); // médio
        const ringExtended = fingerExtended(16, 14); // anelar
        const pinkyExtended = fingerExtended(20, 18); // mindinho

        // Gesto de apontar = indicador esticado + médio, anelar e mindinho dobrados
        const isPointing = indexExtended && !middleExtended && !ringExtended && !pinkyExtended;
        state.isPointing = isPointing;

        if (isPointing) {
            const indexTip = landmarks[8];

            // === MAPEAMENTO CORRETO: unproject Three.js ===
            // Converte a posição 2D da ponta do dedo para NDC (-1 a 1),
            // depois usa unproject para obter a posição exata no mundo 3D.
            const ndcX = (1 - indexTip.x) * 2 - 1;   // espelhar X
            const ndcY = -(indexTip.y * 2 - 1);

            const tipVec = new THREE.Vector3(ndcX, ndcY, 0.5);
            tipVec.unproject(camera);

            // Calcular a direção câmera → ponto no mundo
            const dir = tipVec.sub(camera.position).normalize();

            // Intersectar com o plano Z = 0 (onde as partículas vivem)
            const distToPlane = -camera.position.z / dir.z;
            const worldPos = camera.position.clone().addScaledVector(dir, distToPlane);

            // Suavização do ponteiro (elimina jitter da câmera)
            const smooth = 0.15;
            state.pointerPos.x += (worldPos.x - state.pointerPos.x) * smooth;
            state.pointerPos.y += (worldPos.y - state.pointerPos.y) * smooth;
            state.pointerPos.z = 0;

            // Desenhar esqueleto da mão no overlay fullscreen
            drawHandSkeleton(landmarks);
        } else {
            // Limpar overlay quando não está apontando
            overlayCtx.clearRect(0, 0, handOverlay.width, handOverlay.height);
        }

        // 3. Rotação por inércia (só quando mão fechada)
        const currentX = (1 - landmarks[9].x) * 2 - 1;
        const currentY = -(landmarks[9].y * 2 - 1);

        if (isClosed) {
            state.autoRotate = false;
            const deltaX = currentX - state.prevHandPos.x;
            const deltaY = currentY - state.prevHandPos.y;
            state.rotationVelocity.x = deltaX * 3;
            state.rotationVelocity.y = deltaY * 3;
        }

        state.prevHandPos.x = currentX;
        state.prevHandPos.y = currentY;

        // 4. Zoom por proximidade da mão
        const indexBase = landmarks[5];
        const scaleDist = Math.hypot(indexBase.x - wrist.x, indexBase.y - wrist.y);
        const zoomFactor = (scaleDist - 0.2) * 80;
        state.handZoom += ((-zoomFactor) - state.handZoom) * 0.1;

    } else {
        // Sem mão detectada
        state.isHandClosed = false;
        state.isPointing = false;
        state.autoRotate = true;
        state.handZoom += (0 - state.handZoom) * 0.1;
        // Limpar overlay quando não há mão
        overlayCtx.clearRect(0, 0, handOverlay.width, handOverlay.height);
    }
}

// --- DESENHAR ESQUELETO DA MÃO NO OVERLAY (VISUAL NEON PREMIUM) ---
function drawHandSkeleton(landmarks) {
    overlayCtx.clearRect(0, 0, handOverlay.width, handOverlay.height);

    const W = handOverlay.width;
    const H = handOverlay.height;

    const lx = (lm) => (1 - lm.x) * W;
    const ly = (lm) => lm.y * H;

    // Todos os dedos em verde neon
    const fingerColors = [
        '#39ff14', // polegar
        '#39ff14', // indicador
        '#39ff14', // médio
        '#39ff14', // anelar
        '#39ff14', // mindinho
    ];

    const fingerBones = [
        [0, 1, 2, 3, 4],
        [0, 5, 6, 7, 8],
        [0, 9, 10, 11, 12],
        [0, 13, 14, 15, 16],
        [0, 17, 18, 19, 20]
    ];
    const palmConnections = [[0, 1], [1, 5], [5, 9], [9, 13], [13, 17], [17, 0]];

    // Função para desenhar um segmento com efeito neon (2 passadas: glow + nítido)
    function drawNeonLine(x1, y1, x2, y2, color, width) {
        // Passada 1: glow suave (blur externo)
        overlayCtx.save();
        overlayCtx.shadowColor = color;
        overlayCtx.shadowBlur = 18;
        overlayCtx.strokeStyle = color;
        overlayCtx.lineWidth = width + 3;
        overlayCtx.globalAlpha = 0.35;
        overlayCtx.beginPath();
        overlayCtx.moveTo(x1, y1);
        overlayCtx.lineTo(x2, y2);
        overlayCtx.stroke();

        // Passada 2: linha nítida central
        overlayCtx.shadowBlur = 0;
        overlayCtx.strokeStyle = color;
        overlayCtx.lineWidth = width;
        overlayCtx.globalAlpha = 0.9;
        overlayCtx.beginPath();
        overlayCtx.moveTo(x1, y1);
        overlayCtx.lineTo(x2, y2);
        overlayCtx.stroke();
        overlayCtx.restore();
    }

    function drawNeonCircle(x, y, radius, color, alpha = 1.0) {
        overlayCtx.save();
        overlayCtx.shadowColor = color;
        overlayCtx.shadowBlur = 20;
        overlayCtx.fillStyle = color;
        overlayCtx.globalAlpha = alpha;
        overlayCtx.beginPath();
        overlayCtx.arc(x, y, radius, 0, Math.PI * 2);
        overlayCtx.fill();
        overlayCtx.restore();
    }

    overlayCtx.lineCap = 'round';
    overlayCtx.lineJoin = 'round';

    // Desenhar palma com verde neon
    for (const [a, b] of palmConnections) {
        drawNeonLine(lx(landmarks[a]), ly(landmarks[a]), lx(landmarks[b]), ly(landmarks[b]), '#39ff14', 1.5);
    }

    // Desenhar dedos
    for (let f = 0; f < fingerBones.length; f++) {
        const chain = fingerBones[f];
        const color = fingerColors[f];
        const width = f === 1 ? 3.5 : 2; // indicador mais grosso

        for (let j = 0; j < chain.length - 1; j++) {
            const a = chain[j], b = chain[j + 1];
            drawNeonLine(lx(landmarks[a]), ly(landmarks[a]), lx(landmarks[b]), ly(landmarks[b]), color, width);
        }

        // Juntas: círculos coloridos em cada ponto
        for (let j = 1; j < chain.length; j++) {
            const r = j === chain.length - 1 ? 5 : 3; // ponta maior
            drawNeonCircle(lx(landmarks[chain[j]]), ly(landmarks[chain[j]]), r, color, 0.9);
        }
    }

    // Pulso: círculo especial na base
    drawNeonCircle(lx(landmarks[0]), ly(landmarks[0]), 6, '#ffffff', 0.6);

    // Halo mágico pulsante na ponta do indicador
    const tip = landmarks[8];
    const tx = lx(tip);
    const ty = ly(tip);
    const t = Date.now() * 0.003;
    const pulse = 12 + Math.sin(t) * 5; // raio oscila entre 7 e 17px

    // Anel externo pulsante
    overlayCtx.save();
    overlayCtx.shadowColor = '#39ff14';
    overlayCtx.shadowBlur = 30;
    overlayCtx.strokeStyle = '#39ff14';
    overlayCtx.lineWidth = 2;
    overlayCtx.globalAlpha = 0.6 + Math.sin(t) * 0.3;
    overlayCtx.beginPath();
    overlayCtx.arc(tx, ty, pulse, 0, Math.PI * 2);
    overlayCtx.stroke();
    overlayCtx.restore();

    // Ponto central brilhante
    const grad = overlayCtx.createRadialGradient(tx, ty, 0, tx, ty, 22);
    grad.addColorStop(0, 'rgba(255, 255, 255, 1.0)');
    grad.addColorStop(0.2, 'rgba(57, 255, 20, 0.9)');
    grad.addColorStop(0.6, 'rgba(0, 200, 0, 0.4)');
    grad.addColorStop(1, 'rgba(0, 80, 0, 0)');
    overlayCtx.fillStyle = grad;
    overlayCtx.beginPath();
    overlayCtx.arc(tx, ty, 22, 0, Math.PI * 2);
    overlayCtx.fill();
}

const hands = new Hands({
    locateFile: (file) => {
        return `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}`;
    }
});

hands.setOptions({
    maxNumHands: 1,
    modelComplexity: 1,
    minDetectionConfidence: 0.7,
    minTrackingConfidence: 0.7
});

hands.onResults(onResults);

const cameraUtils = new Camera(videoElement, {
    onFrame: async () => {
        await hands.send({ image: videoElement });
    },
    width: 1280,
    height: 720
});

cameraUtils.start();

// --- LOOP DE ANIMAÇÃO PRINCIPAL ---
function animate() {
    requestAnimationFrame(animate);

    const posAttribute = geometry.attributes.position;
    const currentPositions = posAttribute.array;

    // 1. Gerenciar explosão com Lerp
    const targetExplosion = state.isHandClosed ? 0 : 1;
    state.explosionFactor += (targetExplosion - state.explosionFactor) * 0.05;

    // 2. Física de rotação com inércia
    if (state.isPointing) {
        // Em modo pó mágico: zerar rotação suavemente para alinhar coordenadas com a tela
        particles.rotation.x += (0 - particles.rotation.x) * 0.08;
        particles.rotation.y += (0 - particles.rotation.y) * 0.08;
        state.rotationVelocity.x = 0;
        state.rotationVelocity.y = 0;
    } else {
        particles.rotation.y += state.rotationVelocity.x;
        particles.rotation.x -= state.rotationVelocity.y;
        state.rotationVelocity.x *= 0.95;
        state.rotationVelocity.y *= 0.95;

        if (Math.abs(state.rotationVelocity.x) < 0.001 && state.autoRotate) {
            particles.rotation.y += 0.009; // velocidade Y
            particles.rotation.x += 0.004; // inclinação X
        }
    }

    // 3. Atualizar posição de cada partícula
    for (let i = 0; i < PARTICLE_COUNT; i++) {
        const ix = i * 3;
        const iy = i * 3 + 1;
        const iz = i * 3 + 2;

        const tx = targetPositions[ix];
        const ty = targetPositions[iy];
        const tz = targetPositions[iz];

        const rx = randomPositions[ix];
        const ry = randomPositions[iy];
        const rz = randomPositions[iz];

        const time = Date.now() * 0.001;
        const breathing = Math.sin(time + tx) * 0.05;

        const destX = tx + (rx * state.explosionFactor) + breathing;
        const destY = ty + (ry * state.explosionFactor) + breathing;
        const destZ = tz + (rz * state.explosionFactor);

        if (state.isPointing) {
            // === PÓ MÁGICO ===
            const px = state.pointerPos.x;
            const py = state.pointerPos.y;
            const pz = state.pointerPos.z;

            // Cada partícula orbita em torno do dedo com raio e fase únicos
            const phase = particlePhase[i];
            const orbitR = 1.0 + (phase % 2.5);
            const dustX = px + Math.sin(time * 1.8 + phase) * orbitR;
            const dustY = py + Math.cos(time * 1.4 + phase * 1.2) * orbitR * 0.7;
            const dustZ = pz + Math.sin(time * 1.0 + phase * 0.8) * orbitR * 0.5;

            // Força proporcional à distância (spring-like)
            const dxP = dustX - currentPositions[ix];
            const dyP = dustY - currentPositions[iy];
            const dzP = dustZ - currentPositions[iz];
            const distSq = dxP * dxP + dyP * dyP + dzP * dzP;
            const attraction = 0.025 / (1.0 + distSq * 0.01);

            particleVelocities[ix] += dxP * attraction;
            particleVelocities[ix + 1] += dyP * attraction;
            particleVelocities[ix + 2] += dzP * attraction;

            // Drag alto = rastro longo e suave
            const drag = 0.92;
            particleVelocities[ix] *= drag;
            particleVelocities[ix + 1] *= drag;
            particleVelocities[ix + 2] *= drag;

            currentPositions[ix] += particleVelocities[ix];
            currentPositions[iy] += particleVelocities[ix + 1];
            currentPositions[iz] += particleVelocities[ix + 2];
        } else {
            // MODO NORMAL: animar para a forma alvo
            currentPositions[ix] += (destX - currentPositions[ix]) * 0.08;
            currentPositions[iy] += (destY - currentPositions[iy]) * 0.08;
            currentPositions[iz] += (destZ - currentPositions[iz]) * 0.08;
        }
    }

    posAttribute.needsUpdate = true;

    // 4. Gerenciar zoom
    const targetZoom = state.baseZoom + state.handZoom;
    state.currentZoom += (targetZoom - state.currentZoom) * 0.1;
    if (state.currentZoom < 1) state.currentZoom = 1;
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