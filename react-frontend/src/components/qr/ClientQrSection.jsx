import { useState } from "react";
import { getMyQr } from "../../api/qrApi";
import QrCodeDisplay from "./QrCodeDisplay";

export default function ClientQrSection() {
  const [qrData, setQrData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleShowQr() {
    if (qrData) {
      setQrData(null);
      setError("");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const response = await getMyQr();
      setQrData(response);
    } catch (err) {
      setError(err.message || "Nepodarilo sa načítať QR kód.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section style={{ marginBottom: "32px" }}>
      <h3>Môj QR kód</h3>

      <div style={{ marginBottom: "16px" }}>
        <button onClick={handleShowQr} disabled={loading}>
          {loading
            ? "Načítavam QR kód..."
            : qrData
              ? "Zatvoriť QR kód"
              : "Zobraziť môj QR kód"}
        </button>
      </div>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {qrData && <QrCodeDisplay qrToken={qrData.qrToken} />}
    </section>
  );
}
