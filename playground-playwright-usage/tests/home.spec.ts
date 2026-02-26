import { test, expect } from '@playwright/test';

test('playwright 홈페이지 타이틀 확인', async ({ page }) => {
  await page.goto('https://playwright.dev/');
  await expect(page).toHaveTitle(/Playwright/);
});

test('Get started 문서로 이동', async ({ page }) => {
  await page.goto('https://playwright.dev/');
  await page.getByRole('link', { name: 'Get started' }).click();
  await expect(page).toHaveURL(/.*intro/);
  await expect(page.getByRole('heading', { name: 'Installation' })).toBeVisible();
});
