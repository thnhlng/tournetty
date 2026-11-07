import type { NextConfig } from "next";

// Static Export: Cloudflare liefert nur HTML/CSS/JS aus, alle Daten holt der
// Browser per fetch vom Backend. Kein Server-Runtime, darum kein SSR, keine
// Route Handlers, keine Middleware.
const nextConfig: NextConfig = {
  output: "export",
  images: { unoptimized: true },
};

export default nextConfig;
