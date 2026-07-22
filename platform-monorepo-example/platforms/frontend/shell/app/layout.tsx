import "./globals.css";
import type { ReactNode } from "react";
import { AppLayout } from "@wemeet/ui";
export const metadata = { title: "wemeet 4PL" };
// shell = FE 단일 진입점(호스트). 공통 AppLayout(GNB) 안에 각 모듈 web을 조합.
export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ko">
      <body><AppLayout>{children}</AppLayout></body>
    </html>
  );
}
