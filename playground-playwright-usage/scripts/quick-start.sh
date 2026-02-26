#!/usr/bin/env bash
set -e
npm install
npx playwright install
npm test
