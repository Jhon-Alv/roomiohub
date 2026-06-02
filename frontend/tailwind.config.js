/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      "colors": {
        "surface-container": "#e3efff",
        "on-tertiary-fixed-variant": "#004b72",
        "tertiary-fixed": "#cce5ff",
        "secondary-container": "#fed255",
        "surface-tint": "#475b9c",
        "surface": "#f7f9ff",
        "on-background": "#091d2e",
        "on-tertiary-fixed": "#001e31",
        "surface-variant": "#d1e4fb",
        "on-tertiary-container": "#4497d1",
        "on-primary-fixed-variant": "#2e4382",
        "primary-fixed": "#dce1ff",
        "tertiary": "#001727",
        "outline": "#757681",
        "on-tertiary": "#ffffff",
        "on-secondary-fixed-variant": "#584400",
        "secondary": "#755b00",
        "error-container": "#ffdad6",
        "on-error-container": "#93000a",
        "on-surface": "#091d2e",
        "surface-container-lowest": "#ffffff",
        "surface-container-high": "#d9eaff",
        "on-secondary-container": "#735a00",
        "background": "#f7f9ff",
        "inverse-surface": "#203243",
        "surface-container-highest": "#d1e4fb",
        "secondary-fixed-dim": "#ecc246",
        "surface-container-low": "#edf4ff",
        "outline-variant": "#c5c6d2",
        "error": "#ba1a1a",
        "inverse-on-surface": "#e8f2ff",
        "on-primary-container": "#7a8ed2",
        "surface-dim": "#c9dcf3",
        "on-secondary-fixed": "#241a00",
        "primary-fixed-dim": "#b5c4ff",
        "on-surface-variant": "#444650",
        "inverse-primary": "#b5c4ff",
        "primary": "#00103e",
        "tertiary-fixed-dim": "#91cdff",
        "surface-bright": "#f7f9ff",
        "on-primary": "#ffffff",
        "on-primary-fixed": "#00164d",
        "on-secondary": "#ffffff",
        "secondary-fixed": "#ffe08e",
        "on-error": "#ffffff",
        "tertiary-container": "#002c45",
        "primary-container": "#0a2463"
      },
      "borderRadius": {
        "DEFAULT": "0.25rem",
        "lg": "0.5rem",
        "xl": "0.75rem",
        "full": "9999px"
      },
      "spacing": {
        "gutter": "16px",
        "topbar-height": "64px",
        "unit": "4px",
        "sidebar-width": "260px",
        "container-padding": "24px"
      },
      "fontFamily": {
        "label-md": ["Inter", "sans-serif"],
        "label-sm": ["Inter", "sans-serif"],
        "display": ["Plus Jakarta Sans", "sans-serif"],
        "body-lg": ["Inter", "sans-serif"],
        "h3": ["Plus Jakarta Sans", "sans-serif"],
        "h2": ["Plus Jakarta Sans", "sans-serif"],
        "h1": ["Plus Jakarta Sans", "sans-serif"],
        "body-md": ["Inter", "sans-serif"],
        "body-sm": ["Inter", "sans-serif"],
        "jakarta": ['"Plus Jakarta Sans"', 'sans-serif'],
        "inter": ['Inter', 'sans-serif']
      },
      "fontSize": {
        "label-md": ["14px", { "lineHeight": "20px", "fontWeight": "500" }],
        "label-sm": ["12px", { "lineHeight": "16px", "letterSpacing": "0.05em", "fontWeight": "600" }],
        "display": ["36px", { "lineHeight": "44px", "letterSpacing": "-0.02em", "fontWeight": "700" }],
        "body-lg": ["18px", { "lineHeight": "28px", "fontWeight": "400" }],
        "h3": ["20px", { "lineHeight": "28px", "fontWeight": "600" }],
        "h2": ["24px", { "lineHeight": "32px", "letterSpacing": "-0.01em", "fontWeight": "600" }],
        "h1": ["30px", { "lineHeight": "38px", "letterSpacing": "-0.02em", "fontWeight": "600" }],
        "body-md": ["16px", { "lineHeight": "24px", "fontWeight": "400" }],
        "body-sm": ["14px", { "lineHeight": "20px", "fontWeight": "400" }]
      }
    }
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/container-queries')
  ],
}
