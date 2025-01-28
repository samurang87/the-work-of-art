import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", // Your backend server
        changeOrigin: true, // Ensures the Origin header matches the target
        rewrite: (path) => path.replace(/^\/api/, "/api"), // Optional: adjust path if needed
      },
    },
  },
});
