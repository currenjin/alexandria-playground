import { create } from "zustand";
// 공통 상태관리 = Zustand (요건 §0). 모듈 로컬 스토어.
type OrderState = { count: number; inc: () => void };
export const useOrderStore = create<OrderState>((set) => ({
  count: 0,
  inc: () => set((s) => ({ count: s.count + 1 })),
}));
