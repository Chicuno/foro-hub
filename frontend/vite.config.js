import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/login': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        }
      },
      '/preguntas': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        }
      },
      '/respuestas': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        }
      },
      '/usuarios': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        }
      },
      '/cursos': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        }
      }
    }
  }
})
