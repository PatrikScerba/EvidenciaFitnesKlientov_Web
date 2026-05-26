import { useState } from "react";
import { getEmployeeClientQrCode } from "../../api/qrApi";
import QrCodeDisplay from "./QrCodeDisplay";

export default function EmployeeClientQrSection({ client, onClose }) {
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [qrData, setQrData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleShowQr(event) {
    event.preventDefault();

    if (!client?.clientId) {
      setError("Klient nebol správne vybraný.");
      return;
    }

    if (!securityAnswer.trim()) {
      setError("Zadaj bezpečnostnú odpoveď.");
      return;
    }

    setLoading(true);
    setError("");
    setQrData(null);

    try {
      const response = await getEmployeeClientQrCode({
        email: client.email,
        clientId: client.clientId,
        securityAnswer: securityAnswer.trim(),
      });

      setQrData(response);
    } catch (err) {
      setError(err.message || "Nepodarilo sa zobraziť QR kód klienta.");
    } finally {
      setLoading(false);
    }
  }

  function handleCloseQr() {
    setQrData(null);
    setSecurityAnswer("");
    setError("");

    onClose();
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

        {!qrData && (
          <form onSubmit={handleShowQr}>
            <input
              type="password"
              className="form-control mb-2"
              placeholder="Bezpečnostná odpoveď"
              value={securityAnswer}
              onChange={(e) => setSecurityAnswer(e.target.value)}
            />

            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? "Overujem..." : "Zobraziť QR"}
            </button>
          </form>
        )}

        {qrData && (
          <>
            <button
              type="button"
              className="btn btn-secondary mb-3"
              onClick={handleCloseQr}
            >
              Zatvoriť QR
            </button>

            <QrCodeDisplay qrToken={qrData.qrToken} />
          </>
        )}

        {error && (
          <div style={{ color: "red", marginTop: "12px" }}>{error}</div>
        )}
      </div>
    </div>
  );
}
