/* =========================================================
   Diagrams — minimal box + arrow
   Black bg, bright green (#00ff88)
   ========================================================= */

const C = {
  fg:    '#ffffff',
  dim:   '#a8a8a8',
  mute:  '#5a5a5a',
  line:  'rgba(255,255,255,0.20)',
  g:     '#00ff88',
  bad:   '#ff5577',
};

const arrow = (id, color) => `
  <defs>
    <marker id="${id}" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="${color}"/>
    </marker>
  </defs>`;

function box(x, y, w, h, label, opts = {}) {
  const stroke = opts.stroke || C.line;
  const color  = opts.color  || C.fg;
  const fs     = opts.fs     || 28;
  return `
    <rect x="${x}" y="${y}" width="${w}" height="${h}" rx="4" fill="none" stroke="${stroke}" stroke-width="1.5"/>
    <text x="${x + w/2}" y="${y + h/2 + fs/3}" text-anchor="middle" fill="${color}" font-size="${fs}" font-weight="500" font-family="Pretendard, sans-serif" letter-spacing="-0.01em">${label}</text>
  `;
}

/* ---------- Split services (each with own DB) ---------- */
function dSplit() {
  return `
<svg viewBox="0 0 1640 440" xmlns="http://www.w3.org/2000/svg">
  ${arrow('asp', C.g)}

  <!-- Three services, each with own DB stacked below -->
  ${box(80, 60, 340, 110, '주문', { stroke: C.g, color: C.fg, fs: 36 })}
  ${box(650, 60, 340, 110, '재고', { stroke: C.g, color: C.fg, fs: 36 })}
  ${box(1220, 60, 340, 110, '결제', { stroke: C.g, color: C.fg, fs: 36 })}

  <!-- DBs as cylinders -->
  ${cylinder(130, 280, 240, 100, 'orders DB', C.g)}
  ${cylinder(700, 280, 240, 100, 'inventory DB', C.g)}
  ${cylinder(1270, 280, 240, 100, 'payments DB', C.g)}

  <!-- Service -> DB links -->
  <path d="M 250 170 L 250 280" stroke="${C.g}" stroke-width="1.8" marker-end="url(#asp)"/>
  <path d="M 820 170 L 820 280" stroke="${C.g}" stroke-width="1.8" marker-end="url(#asp)"/>
  <path d="M 1390 170 L 1390 280" stroke="${C.g}" stroke-width="1.8" marker-end="url(#asp)"/>

  <!-- Horizontal divider showing no cross-DB access -->
  <line x1="420" y1="335" x2="700" y2="335" stroke="${C.mute}" stroke-width="1.5" stroke-dasharray="6 8"/>
  <line x1="940" y1="335" x2="1220" y2="335" stroke="${C.mute}" stroke-width="1.5" stroke-dasharray="6 8"/>
</svg>`;
}

function cylinder(x, y, w, h, label, color) {
  const ry = 14;
  return `
    <path d="M ${x} ${y+ry} A ${w/2} ${ry} 0 0 1 ${x+w} ${y+ry} L ${x+w} ${y+h-ry} A ${w/2} ${ry} 0 0 1 ${x} ${y+h-ry} Z"
      fill="none" stroke="${color}" stroke-width="1.5"/>
    <path d="M ${x} ${y+ry} A ${w/2} ${ry} 0 0 0 ${x+w} ${y+ry}" fill="none" stroke="${color}" stroke-width="1.5"/>
    <text x="${x + w/2}" y="${y + h/2 + 10}" text-anchor="middle" fill="${color}"
      font-size="26" font-family="JetBrains Mono, monospace" font-weight="500">${label}</text>
  `;
}

/* ---------- 2PC ---------- */
function d2pc() {
  return `
<svg viewBox="0 0 1640 620" xmlns="http://www.w3.org/2000/svg">
  ${arrow('a2pc', C.g)}

  ${box(620, 40, 400, 100, 'Coordinator', { stroke: C.g, color: C.g, fs: 34 })}

  ${box(120, 480, 320, 100, 'Participant A', { stroke: C.line, fs: 28 })}
  ${box(660, 480, 320, 100, 'Participant B', { stroke: C.line, fs: 28 })}
  ${box(1200, 480, 320, 100, 'Participant C', { stroke: C.line, fs: 28 })}

  <!-- Phase 1 label -->
  <text x="120" y="230" fill="${C.g}" font-size="26" letter-spacing="4" font-weight="500" font-family="JetBrains Mono, monospace">PHASE 1 · PREPARE</text>
  <path d="M 720 140 Q 500 300 280 480" fill="none" stroke="${C.g}" stroke-width="1.8" marker-end="url(#a2pc)"/>
  <path d="M 820 140 L 820 480" stroke="${C.g}" stroke-width="1.8" marker-end="url(#a2pc)"/>
  <path d="M 920 140 Q 1140 300 1360 480" fill="none" stroke="${C.g}" stroke-width="1.8" marker-end="url(#a2pc)"/>

  <!-- Phase 2 label -->
  <text x="60" y="610" fill="${C.dim}" font-size="26" letter-spacing="4" font-weight="500" font-family="JetBrains Mono, monospace">PHASE 2 · COMMIT / ABORT</text>
</svg>`;
}

/* ---------- Saga core ---------- */
function dSaga() {
  return `
<svg viewBox="0 0 1640 440" xmlns="http://www.w3.org/2000/svg">
  ${arrow('asg', C.g)}
  ${arrow('asgb', C.bad)}

  ${box(40, 80, 300, 110, 'T1 주문', { stroke: C.g, color: C.fg, fs: 34 })}
  ${box(420, 80, 300, 110, 'T2 재고', { stroke: C.g, color: C.fg, fs: 34 })}
  ${box(800, 80, 300, 110, 'T3 결제', { stroke: C.bad, color: C.bad, fs: 34 })}
  ${box(1180, 80, 420, 110, 'T4 배송', { stroke: C.mute, color: C.mute, fs: 34 })}

  <path d="M 340 135 L 420 135" stroke="${C.g}" stroke-width="2" marker-end="url(#asg)"/>
  <path d="M 720 135 L 800 135" stroke="${C.g}" stroke-width="2" marker-end="url(#asg)"/>

  <!-- Compensation -->
  <text x="40" y="290" fill="${C.bad}" font-size="26" letter-spacing="4" font-weight="500" font-family="JetBrains Mono, monospace">COMPENSATE · 역순 실행</text>
  ${box(40, 310, 300, 100, 'C1 주문 취소', { stroke: C.bad, color: C.bad, fs: 28 })}
  ${box(420, 310, 300, 100, 'C2 재고 반납', { stroke: C.bad, color: C.bad, fs: 28 })}

  <path d="M 420 360 L 340 360" stroke="${C.bad}" stroke-width="2" marker-end="url(#asgb)"/>
  <path d="M 950 190 Q 950 260 720 360" fill="none" stroke="${C.bad}" stroke-width="2" stroke-dasharray="4 4" marker-end="url(#asgb)"/>
</svg>`;
}

/* ---------- Choreography: Event Broker fan-out to services (each with DB) ---------- */
function dChoreo() {
  // Compact service + DB column
  const unit = (x, label) => `
    <circle cx="${x}" cy="300" r="52" fill="none" stroke="${C.g}" stroke-width="1.6"/>
    <text x="${x}" y="308" text-anchor="middle" fill="${C.fg}" font-size="26" font-weight="500" font-family="Pretendard, sans-serif">${label}</text>
    <path d="M ${x} 200 L ${x} 248" stroke="${C.g}" stroke-width="1.6" marker-end="url(#ach1)" marker-start="url(#ach1s)"/>
    <path d="M ${x} 352 L ${x} 430" stroke="${C.g}" stroke-width="1.6" marker-end="url(#ach1)" marker-start="url(#ach1s)"/>
    ${cylinder(x-50, 430, 100, 80, '', C.g)}`;

  return `
<svg viewBox="0 0 1640 620" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <marker id="ach1" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="${C.g}"/>
    </marker>
    <marker id="ach1s" viewBox="0 0 10 10" refX="1" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
      <path d="M 10 0 L 0 5 L 10 10 z" fill="${C.g}"/>
    </marker>
  </defs>

  <text x="40" y="34" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">중앙 조정자 없음 · 각 서비스가 Broker를 통해 이벤트를 발행·구독</text>

  <!-- EVENT BROKER (top bar) -->
  <rect x="240" y="100" width="1160" height="96" rx="4" fill="none" stroke="${C.g}" stroke-width="1.8"/>
  <text x="820" y="156" text-anchor="middle" fill="${C.g}" font-size="34" font-weight="600" font-family="Pretendard, sans-serif" letter-spacing="0.05em">EVENT BROKER</text>

  <!-- 4 services, each with DB -->
  ${unit(380, '주문')}
  ${unit(680, '재고')}
  ${unit(980, '결제')}
  ${unit(1280, '배송')}
</svg>`;
}

/* ---------- Orchestration: Orchestrator as hub, services hang off it (tree) ---------- */
function dOrch() {
  const unit = (x, y, label) => `
    <circle cx="${x}" cy="${y}" r="52" fill="none" stroke="${C.g}" stroke-width="1.6"/>
    <text x="${x}" y="${y+8}" text-anchor="middle" fill="${C.fg}" font-size="26" font-weight="500" font-family="Pretendard, sans-serif">${label}</text>
    <path d="M ${x} ${y+52} L ${x} ${y+120}" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aor1)" marker-start="url(#aor1s)"/>
    ${cylinder(x-50, y+120, 100, 70, '', C.g)}`;

  return `
<svg viewBox="0 0 1640 620" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <marker id="aor1" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="${C.g}"/>
    </marker>
    <marker id="aor1s" viewBox="0 0 10 10" refX="1" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
      <path d="M 10 0 L 0 5 L 10 10 z" fill="${C.g}"/>
    </marker>
  </defs>

  <text x="40" y="34" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">Orchestrator가 각 서비스에게 명령·응답을 직접 주고받으며 흐름을 제어</text>

  <!-- ORCHESTRATOR (center top) -->
  <circle cx="820" cy="150" r="70" fill="none" stroke="${C.g}" stroke-width="2"/>
  <text x="820" y="145" text-anchor="middle" fill="${C.g}" font-size="24" font-weight="600" font-family="Pretendard, sans-serif">Orchestra-</text>
  <text x="820" y="173" text-anchor="middle" fill="${C.g}" font-size="24" font-weight="600" font-family="Pretendard, sans-serif">tor</text>

  <!-- Branches to 3 services -->
  <path d="M 770 200 L 460 330" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aor1)" marker-start="url(#aor1s)"/>
  <path d="M 820 220 L 820 330" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aor1)" marker-start="url(#aor1s)"/>
  <path d="M 870 200 L 1180 330" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aor1)" marker-start="url(#aor1s)"/>

  <!-- Services with DBs -->
  ${unit(460, 382, '재고')}
  ${unit(820, 382, '결제')}
  ${unit(1180, 382, '배송')}
</svg>`;
}

/* ---------- Dual Write problem ---------- */
function dDualWrite() {
  return `
<svg viewBox="0 0 1640 520" xmlns="http://www.w3.org/2000/svg">
  ${arrow('adw', C.g)}
  ${arrow('adwb', C.bad)}

  ${box(80, 200, 280, 120, 'Service', { stroke: C.line, fs: 34 })}
  ${box(780, 60, 280, 120, 'DB', { stroke: C.g, color: C.g, fs: 34 })}
  ${box(780, 340, 280, 120, 'Kafka', { stroke: C.line, color: C.dim, fs: 34 })}

  <!-- Step 1: success to DB -->
  <path d="M 360 240 Q 570 240 780 120" fill="none" stroke="${C.g}" stroke-width="2" marker-end="url(#adw)"/>
  <text x="430" y="180" fill="${C.g}" font-size="26" font-family="JetBrains Mono, monospace">① DB commit ✓</text>

  <!-- Step 2: failed to Kafka -->
  <path d="M 360 290 Q 570 290 780 400" fill="none" stroke="${C.bad}" stroke-width="2" stroke-dasharray="8 6" marker-end="url(#adwb)"/>
  <text x="430" y="490" fill="${C.bad}" font-size="26" font-family="JetBrains Mono, monospace">② 프로세스 크래시 · 이벤트 유실</text>
</svg>`;
}

/* ---------- Outbox pattern ---------- */
function dOutbox() {
  return `
<svg viewBox="0 0 1640 500" xmlns="http://www.w3.org/2000/svg">
  ${arrow('aox', C.g)}

  ${box(40, 200, 240, 120, 'Service', { stroke: C.line, fs: 32 })}

  <!-- SINGLE TX container -->
  <rect x="380" y="110" width="460" height="300" rx="4" fill="none" stroke="${C.g}" stroke-width="1.5" stroke-dasharray="4 4"/>
  <text x="610" y="90" text-anchor="middle" fill="${C.g}" font-size="26" font-family="JetBrains Mono, monospace" letter-spacing="3">SINGLE TX</text>
  ${box(420, 170, 380, 80, 'orders', { stroke: C.line, fs: 28 })}
  ${box(420, 290, 380, 80, 'outbox', { stroke: C.g, color: C.g, fs: 28 })}

  ${box(940, 200, 240, 120, 'CDC', { stroke: C.g, color: C.g, fs: 32 })}
  ${box(1320, 200, 280, 120, 'Kafka', { stroke: C.line, fs: 32 })}

  <!-- Arrows: Service enters TX box (ok to cross) -->
  <path d="M 280 260 L 420 210" stroke="${C.g}" stroke-width="2" marker-end="url(#aox)"/>
  <path d="M 280 260 L 420 330" stroke="${C.g}" stroke-width="2" marker-end="url(#aox)"/>
  <!-- CDC reads outbox (starts from outside the box) -->
  <path d="M 840 330 Q 890 330 940 280" fill="none" stroke="${C.g}" stroke-width="2" marker-end="url(#aox)"/>
  <path d="M 1180 260 L 1320 260" stroke="${C.g}" stroke-width="2" marker-end="url(#aox)"/>

  <text x="870" y="385" fill="${C.dim}" font-size="26" font-family="JetBrains Mono, monospace">poll</text>
</svg>`;
}

/* ---------- Decision tree ---------- */
function dDecision() {
  return `
<svg viewBox="0 0 1640 560" xmlns="http://www.w3.org/2000/svg">
  ${arrow('adt', C.g)}

  <!-- Root -->
  ${box(560, 20, 520, 90, '강한 일관성 필수?', { stroke: C.g, color: C.fg, fs: 30 })}

  <!-- Level 1 -->
  ${box(140, 230, 380, 90, '2PC · TCC', { stroke: C.g, color: C.g, fs: 32 })}
  ${box(1120, 230, 380, 90, '한 곳에서 흐름 보기?', { stroke: C.g, color: C.fg, fs: 30 })}

  <!-- Level 2 (only under the right branch) -->
  ${box(900, 440, 340, 90, 'Orchestration', { stroke: C.g, color: C.g, fs: 30 })}
  ${box(1280, 440, 340, 90, 'Choreography', { stroke: C.g, color: C.g, fs: 30 })}

  <!-- Root → Level 1 edges -->
  <path d="M 700 110 L 380 230" stroke="${C.g}" stroke-width="1.8" marker-end="url(#adt)"/>
  <path d="M 940 110 L 1260 230" stroke="${C.g}" stroke-width="1.8" marker-end="url(#adt)"/>

  <!-- Level 1 (right) → Level 2 edges -->
  <path d="M 1240 320 L 1070 440" stroke="${C.g}" stroke-width="1.8" marker-end="url(#adt)"/>
  <path d="M 1380 320 L 1450 440" stroke="${C.g}" stroke-width="1.8" marker-end="url(#adt)"/>

  <!-- Labels, offset from edge lines -->
  <text x="470" y="200" fill="${C.g}" font-size="26" font-family="JetBrains Mono, monospace">YES</text>
  <text x="1140" y="200" fill="${C.dim}" font-size="26" font-family="JetBrains Mono, monospace">NO</text>
  <text x="1090" y="410" fill="${C.g}" font-size="26" font-family="JetBrains Mono, monospace">YES</text>
  <text x="1430" y="410" fill="${C.dim}" font-size="26" font-family="JetBrains Mono, monospace">NO</text>
</svg>`;
}

/* ---------- Choreography FAILURE: chain with compensations ---------- */
function dChoreoFail() {
  const svc = (x, y, label) => `
    <rect x="${x}" y="${y}" width="220" height="90" rx="4" fill="none" stroke="${C.g}" stroke-width="1.6"/>
    <text x="${x+110}" y="${y+56}" text-anchor="middle" fill="${C.fg}" font-size="28" font-weight="500" font-family="Pretendard, sans-serif">${label}</text>`;

  return `
<svg viewBox="0 0 1640 620" xmlns="http://www.w3.org/2000/svg">
  ${arrow('acf', C.g)}
  ${arrow('acfb', C.bad)}

  <text x="40" y="34" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">결제 실패 → PaymentFailed 이벤트 → 재고·주문이 각자 구독해서 보상</text>

  <!-- Services in a row -->
  ${svc(80,  200, '주문')}
  ${svc(530, 200, '재고')}
  ${svc(980, 200, '결제')}
  <rect x="1340" y="200" width="220" height="90" rx="4" fill="none" stroke="${C.bad}" stroke-width="2"/>
  <text x="1450" y="256" text-anchor="middle" fill="${C.bad}" font-size="28" font-weight="600" font-family="Pretendard, sans-serif">결제 ✗</text>

  <!-- Forward chain (green, above) -->
  <path d="M 300 225 L 530 225" stroke="${C.g}" stroke-width="1.8" marker-end="url(#acf)"/>
  <text x="415" y="210" text-anchor="middle" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">OrderCreated</text>

  <path d="M 750 225 L 980 225" stroke="${C.g}" stroke-width="1.8" marker-end="url(#acf)"/>
  <text x="865" y="186" text-anchor="middle" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">InventoryReserved</text>

  <path d="M 1200 225 L 1340 225" stroke="${C.g}" stroke-width="1.8" marker-end="url(#acf)"/>
  <text x="1270" y="186" text-anchor="middle" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">PayRequest</text>

  <!-- Failure event emitted (down from 결제 ✗) -->
  <path d="M 1450 290 L 1450 430" stroke="${C.bad}" stroke-width="2" marker-end="url(#acfb)"/>
  <text x="1190" y="365" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">PaymentFailed 발행</text>

  <!-- Event broker bar -->
  <rect x="160" y="430" width="1360" height="60" rx="4" fill="none" stroke="${C.bad}" stroke-width="1.8"/>
  <text x="840" y="468" text-anchor="middle" fill="${C.bad}" font-size="26" font-weight="600" font-family="Pretendard, sans-serif" letter-spacing="0.05em">EVENT BROKER · PaymentFailed</text>

  <!-- Subscribers: 재고 and 주문 pull the failure event -->
  <path d="M 640 430 L 640 290" fill="none" stroke="${C.bad}" stroke-width="1.8" stroke-dasharray="6 6" marker-end="url(#acfb)"/>
  <text x="760" y="360" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">재고 취소 (보상)</text>

  <path d="M 190 430 L 190 290" fill="none" stroke="${C.bad}" stroke-width="1.8" stroke-dasharray="6 6" marker-end="url(#acfb)"/>
  <text x="310" y="360" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">주문 취소 (보상)</text>

  <!-- Legend -->
  <line x1="60" y1="570" x2="120" y2="570" stroke="${C.g}" stroke-width="1.8"/>
  <text x="135" y="578" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">정방향 이벤트</text>
  <line x1="380" y1="570" x2="440" y2="570" stroke="${C.bad}" stroke-width="1.8" stroke-dasharray="6 6"/>
  <text x="455" y="578" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">실패 이벤트 · 보상 트랜잭션</text>
</svg>`;
}

/* ---------- Orchestration FAILURE: orchestrator sends reverse compensating commands ---------- */
function dOrchFail() {
  const svc = (x, y, label, bad) => {
    const stroke = bad ? C.bad : C.g;
    const color  = bad ? C.bad : C.fg;
    return `
      <rect x="${x}" y="${y}" width="220" height="90" rx="4" fill="none" stroke="${stroke}" stroke-width="${bad?2:1.6}"/>
      <text x="${x+110}" y="${y+56}" text-anchor="middle" fill="${color}" font-size="28" font-weight="${bad?600:500}" font-family="Pretendard, sans-serif">${label}${bad?' ✗':''}</text>`;
  };

  return `
<svg viewBox="0 0 1640 620" xmlns="http://www.w3.org/2000/svg">
  ${arrow('aof', C.g)}
  ${arrow('aofb', C.bad)}

  <text x="40" y="34" fill="${C.dim}" font-size="24" font-family="JetBrains Mono, monospace">결제 실패 reply → Orchestrator가 역순으로 CancelInventory · CancelOrder 명령</text>

  <!-- Orchestrator at top center -->
  <rect x="660" y="80" width="320" height="100" rx="4" fill="none" stroke="${C.g}" stroke-width="2"/>
  <text x="820" y="138" text-anchor="middle" fill="${C.g}" font-size="30" font-weight="600" font-family="Pretendard, sans-serif">Orchestrator</text>

  <!-- Services row (middle) -->
  ${svc(160,  310, '주문')}
  ${svc(710,  310, '재고')}
  ${svc(1260, 310, '결제', true)}

  <!-- Forward commands (green, downward from orchestrator to each service) -->
  <path d="M 720 180 L 330 310" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aof)"/>
  <text x="180" y="220" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">① CreateOrder</text>

  <path d="M 820 180 L 820 310" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aof)"/>
  <text x="840" y="250" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">② ReserveInventory</text>

  <path d="M 920 180 L 1310 310" stroke="${C.g}" stroke-width="1.6" marker-end="url(#aof)"/>
  <text x="1230" y="220" fill="${C.g}" font-size="24" font-family="JetBrains Mono, monospace">③ Pay</text>

  <!-- Failure reply up from 결제 ✗ -->
  <path d="M 1310 310 L 960 180" stroke="${C.bad}" stroke-width="2" marker-end="url(#aofb)"/>
  <text x="1020" y="295" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">④ PaymentFailed</text>

  <!-- Compensations: route AROUND the left side, below services -->
  <path d="M 670 150 Q 500 150 500 500 L 820 500 L 820 400" fill="none" stroke="${C.bad}" stroke-width="1.8" stroke-dasharray="6 6" marker-end="url(#aofb)"/>
  <text x="540" y="540" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">⑤ CancelInventory</text>

  <path d="M 670 130 Q 420 130 420 575 L 270 575 L 270 400" fill="none" stroke="${C.bad}" stroke-width="1.8" stroke-dasharray="6 6" marker-end="url(#aofb)"/>
  <text x="60" y="540" fill="${C.bad}" font-size="24" font-family="JetBrains Mono, monospace">⑥ CancelOrder</text>
</svg>`;
}

/* ---------- Mount ---------- */
function mount(id, html) {
  const el = document.getElementById(id);
  if (el) el.innerHTML = html;
}

mount('d-split',     dSplit());
mount('d-2pc',       d2pc());
mount('d-saga',      dSaga());
mount('d-choreo',      dChoreo());
mount('d-choreo-fail', dChoreoFail());
mount('d-orch',        dOrch());
mount('d-orch-fail',   dOrchFail());
mount('d-dualwrite', dDualWrite());
mount('d-outbox',    dOutbox());
mount('d-decision',  dDecision());
