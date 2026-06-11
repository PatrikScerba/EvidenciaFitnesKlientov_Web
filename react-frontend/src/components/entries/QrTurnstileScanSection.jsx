import { useState } from "react";

import { processQrTurnstileScan } from "../../api/qrTurnstileScanApi";
import CameraQrScanner from "../entries/CameraQrScanner";

export default function QrTurnstileScanSection() {
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [scanResult, setScanResult] = useState(null);

  function getQrScanErrorMessage(reason) {
    switch (reason) {
      case "NO_MEMBERSHIP":
        return "Klient nemá aktívnu permanentku.";
      case "ALREADY_ENTERED_TODAY":
        return "Klient už dnes má zaznamenaný vstup do fitnescentra.";
      case "MEMBERSHIP_EXPIRED":
        return "Permanentka klienta je expirovaná.";
      default:
        return "QR scan bol zamietnutý z neznámeho dôvodu.";
    }
  }

  function getStatusMessage(status) {
    switch (status) {
      case "APPROVED":
        return "Povolený";
      case "DENIED":
        return "Zamietnutý";
      default:
        return "Neznámy stav";
    }
  }

  function getQrScanSuccessMessage(response) {
    if (response.status !== "APPROVED") {
      return "";
    }

    if (response.departureTime) {
      return "Odchod klienta bol úspešne zaznamenaný.";
    }

    return "Vstup klienta bol úspešne zaznamenaný.";
  }

  async function processQrScan(token) {
    const cleanToken = token.trim();

    if (!cleanToken) {
      return;
    }

    setScanResult(null);
    setMessage("");
    setError("");

    try {
      const response = await processQrTurnstileScan(cleanToken);
      setScanResult(response);

      if (response.status === "APPROVED") {
        setMessage(getQrScanSuccessMessage(response));
      } else {
        setMessage("");
        setError("QR scan bol zamietnutý.");
      }
    } catch (err) {
      setMessage("");
      setError(err.message || "QR scan sa nepodarilo spracovať.");
    }
  }

  async function handleCameraQrScan(scannedToken) {
    await processQrScan(scannedToken);
  }
  function clearScanState(){
    setMessage("");
    setError("");
    setScanResult(null);
  }

  return (
    <section>
      <h3>Turniketový režim</h3>

      <p>
        Spusti kameru a naskenuj QR kód klienta. Systém overí platnosť QR kódu,
        skontroluje stav permanentky a automaticky rozhodne, či ide o vstup
        alebo odchod.
      </p>

      <CameraQrScanner
        onScanSuccess={handleCameraQrScan}
        onScannerStop={clearScanState}
      />

      {message && (
        <p style={{ color: "green", fontWeight: "bold" }}>{message}</p>
      )}

      {error && <p style={{ color: "red", fontWeight: "bold" }}>{error}</p>}

      {scanResult && (
        <div>
          <p>
            Klient: {scanResult.firstName} {scanResult.lastName}
          </p>

          <p>ID: {scanResult.clientId}</p>

          {scanResult.arrivalTime && (
            <p>Čas príchodu: {scanResult.arrivalTime}</p>
          )}

          {scanResult.departureTime && (
            <p>Čas odchodu: {scanResult.departureTime}</p>
          )}

          {scanResult.status === "DENIED" && (
            <>
              <p>Stav: {getStatusMessage(scanResult.status)}</p>
              <p>Dôvod: {getQrScanErrorMessage(scanResult.reason)}</p>
            </>
          )}
        </div>
      )}
    </section>
  );
}
