import { test, expect } from '@playwright/test';

test('TodoMVC 입력 및 완료 체크', async ({ page }) => {
  await page.goto('https://demo.playwright.dev/todomvc');

  const input = page.getByPlaceholder('What needs to be done?');
  await input.fill('Playwright 공부');
  await input.press('Enter');
  await input.fill('E2E 시나리오 작성');
  await input.press('Enter');

  const items = page.getByTestId('todo-item');
  await expect(items).toHaveCount(2);

  await items.nth(0).getByRole('checkbox').check();
  await expect(items.nth(0)).toHaveClass(/completed/);
});
