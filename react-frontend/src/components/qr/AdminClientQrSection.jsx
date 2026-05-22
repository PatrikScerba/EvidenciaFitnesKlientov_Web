import { useState } from "react";
import { getAdminClientQrCode } from "../../api/qrApi";
import QrCodeDisplay from "./QrCodeDisplay";

export default function AdminClientQrSection({ client, onClose }) {
  const [qrData, setQrData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleShowQr() {
    if (!client?.clientId) {
      setError("Klient nebol správne vybraný.");
      return;
    }

    setLoading(true);
    setError("");
    setQrData(null);

    try {
      const response = await getAdminClientQrCode(client.clientId);
      setQrData(response);
    } catch (err) {
      setError(err.message || "Nepodarilo sa načítať QR kód klienta.");
    } finally {
      setLoading(false);
    }
  }

  if (!client?.clientId) {
    return null;
  }

  return (
    <div className="card mt-3">
      <div className="card-body">
        <h5 className="card-title">QR kód klienta</h5>

        <p className="mb-2">
          <strong>Klient:</strong> {client.firstName} {client.lastName}
          <br />
          <strong>Email:</strong> {client.email}
        </p>

        <button
          type="button"
          className="btn btn-primary mb-3"
          onClick={qrData ? onClose : handleShowQr}
          disabled={loading}
        >
          {loading ? "Načítavam..." : qrData ? "Zatvoriť QR" : "Zobraziť QR"}
        </button>

        {error && <div className="alert alert-danger">{error}</div>}

        {qrData && <QrCodeDisplay qrToken={qrData.qrToken} />}
      </div>
    </div>
  );
}
