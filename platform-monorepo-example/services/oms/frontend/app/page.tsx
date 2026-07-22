"use client";
import { useForm } from "react-hook-form";
import { Button } from "@wemeet/ui";
import { formatMoney } from "@wemeet/lib";
import { useOrderStore } from "../store/orderStore";

type FormValues = { amount: string };

export default function Page() {
  const { count, inc } = useOrderStore();
  const { register, handleSubmit } = useForm<FormValues>({ defaultValues: { amount: "1250000" } });
  // 요건 스택 데모: Next(CSR) · Zustand · React Hook Form · Fetch(@wemeet/lib) · Shadcn(@wemeet/ui) · Tailwind
  // (AG Grid·Mapbox는 유료·API 키 필요 → 의존만 선언, 화면 데모 제외)
  return (
    <main className="space-y-4 p-8">
      <h1 className="text-2xl font-bold">wemeet OMS — frontend (Next.js CSR)</h1>
      <p className="text-gray-600">확정 주문 수: {count}</p>
      <form onSubmit={handleSubmit((v) => alert(formatMoney(v.amount, "KRW")))} className="space-x-2">
        <input {...register("amount")} className="rounded border px-2 py-1" />
        <Button type="submit">금액 표시</Button>
      </form>
      <Button onClick={inc}>주문 확정 +1</Button>
    </main>
  );
}
