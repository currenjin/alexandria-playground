// 공통 유틸·API 클라이언트·i18n (모든 모듈 frontend 공용).
export const apiClient = (base: string) => ({
  get: (path: string) => fetch(`${base}${path}`).then((r) => r.json()),
});
export const formatMoney = (v: string, currency: string) =>
  new Intl.NumberFormat("ko-KR", { style: "currency", currency }).format(Number(v));
