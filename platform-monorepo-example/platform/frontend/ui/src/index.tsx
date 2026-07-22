import type { ButtonHTMLAttributes, ReactNode } from "react";
// 공통 UI 컴포넌트 (Shadcn 래핑 자리). 모든 모듈 frontend가 workspace로 소비.
export function Button({ children, ...props }: ButtonHTMLAttributes<HTMLButtonElement> & { children: ReactNode }) {
  return (
    <button className="wm-btn rounded bg-black px-3 py-1 text-white hover:opacity-80" {...props}>
      {children}
    </button>
  );
}
