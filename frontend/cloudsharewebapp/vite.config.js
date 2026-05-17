import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],

  server: {
    headers: {
      'Content-Security-Policy': [
        "default-src 'self'",

        // Scripts
        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://*.clerk.accounts.dev https://*.accounts.dev",

        // Styles
        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",

        // Fonts
        "font-src 'self' https://fonts.gstatic.com data:",

        // Images
        "img-src 'self' data: blob: https:",

        // API / Fetch / Clerk
        "connect-src 'self' http://localhost:8080 https://*.clerk.accounts.dev https://*.accounts.dev https://api.clerk.com",

        // Clerk iframe
        "frame-src 'self' https://*.clerk.accounts.dev https://*.accounts.dev",

        // Web workers
        "worker-src 'self' blob:"
      ].join('; ')
    }
  }
})