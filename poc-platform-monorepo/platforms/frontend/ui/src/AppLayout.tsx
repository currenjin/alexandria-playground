import type { ReactNode } from "react";
import { menu } from "./menu";
// 공통 셸 레이아웃: GNB(전역 내비) + 모듈 탭. shell과 각 모듈 web이 공유.
export function AppLayout({ children }: { children: ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="flex items-center gap-6 border-b bg-white px-6 py-3">
        <span className="text-lg font-bold">wemeet</span>
        <nav className="flex gap-4">
          {menu.map((m) => (
            <a key={m.key} href={m.href} className="text-gray-700 hover:text-black">{m.label}</a>
          ))}
        </nav>
      </header>
      <main className="p-6">{children}</main>
    </div>
  );
}
