// 공통 유틸·API·세션·탭 상태.
export const apiClient = (base: string) => ({
  get: (path: string) => fetch(`${base}${path}`).then((r) => r.json()),
});
export const formatMoney = (v: string, currency: string) =>
  new Intl.NumberFormat("ko-KR", { style: "currency", currency }).format(Number(v));

export type Session = { userId: string; tenantId: string } | null;
export function getSession(): Session {
  if (typeof document === "undefined") return null;
  return { userId: "demo", tenantId: "dongsuh" };
}
const TABS_KEY = "wm_tabs";
export const tabsCookie = {
  read(): string[] {
    if (typeof document === "undefined") return [];
    const m = document.cookie.match(new RegExp(`${TABS_KEY}=([^;]+)`));
    return m ? decodeURIComponent(m[1]).split(",").filter(Boolean) : [];
  },
  write(tabs: string[]): void {
    if (typeof document === "undefined") return;
    document.cookie = `${TABS_KEY}=${encodeURIComponent(tabs.join(","))};path=/`;
  },
};
