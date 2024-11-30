/* eslint-disable no-undef */
import react from "@vitejs/plugin-react";
import "dotenv/config";
import { defineConfig } from "vite";

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            "/api": {
                target: "http://10.10.102.114:8081",
                changeOrigin: true,
                secure: false,
            },
        },
    },
});
