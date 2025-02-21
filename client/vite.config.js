import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // ✅ Make sure it's localhost, NOT 0.0.0.0
        changeOrigin: true,
        secure: false, // In case HTTPS causes issues
        rewrite: (path) => path.replace(/^\/api/, ''), // ✅ Removes '/api' before forwarding
      },
    },
  },
});

