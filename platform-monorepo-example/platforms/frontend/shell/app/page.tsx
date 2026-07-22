"use client";
import { getSession } from "@wemeet/lib";
export default function Home() {
  const s = getSession();
  return (
    <div>
      <h1 className="text-2xl font-bold">wemeet 4PL — shell</h1>
      <p className="text-gray-600">세션: {s ? `${s.userId} @ ${s.tenantId}` : "없음"}</p>
      <p className="mt-2 text-sm text-gray-500">상단 GNB에서 모듈로 이동. 각 모듈 web은 자기 경로(/oms 등)에서 렌더.</p>
    </div>
  );
}
