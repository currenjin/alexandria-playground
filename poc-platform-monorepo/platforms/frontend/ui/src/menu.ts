// 전역 메뉴 정의 (GNB·탭).
export type MenuItem = { key: string; label: string; href: string };
export const menu: MenuItem[] = [
  { key: "oms", label: "주문", href: "/oms" },
  { key: "tms", label: "운송", href: "/tms" },
  { key: "bms", label: "정산", href: "/bms" },
  { key: "ems", label: "증빙", href: "/ems" },
];
