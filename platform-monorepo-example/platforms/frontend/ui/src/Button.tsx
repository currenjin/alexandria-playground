import type { ButtonHTMLAttributes, ReactNode } from "react";
export function Button({ children, ...props }: ButtonHTMLAttributes<HTMLButtonElement> & { children: ReactNode }) {
  return (
    <button className="wm-btn rounded bg-black px-3 py-1 text-white hover:opacity-80" {...props}>
      {children}
    </button>
  );
}
