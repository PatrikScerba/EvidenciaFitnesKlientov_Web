import { useRef, useState } from "react";

import { processQrServiceScan } from "../../api/qrServiceScanApi";

export default function QrServiceScanSection() {
  const [qrToken, setQrToken] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [scanResult, setScanResult] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const isProcessingRef = useRef(false);
  const inputRef = useRef(null);

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

    if (!cleanToken || isProcessingRef.current) {
      return;
    }

    isProcessingRef.current = true;
    setIsProcessing(true);
    setScanResult(null);
    setMessage("");
    setError("");

    try {
      const response = await processQrServiceScan(cleanToken);
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
    } finally {
      setQrToken("");

      setTimeout(() => {
        isProcessingRef.current = false;
        setIsProcessing(false);
        inputRef.current?.focus();
      }, 1500);
    }
  }
  async function handleQrServiceScan(event) {
    event.preventDefault();
    await processQrScan(qrToken);
  }

  return (
    <section>
      <h3>Servisný QR scan</h3>

      <form onSubmit={handleQrServiceScan}>
        <input
          ref={inputRef}
          autoFocus
          autoComplete="off"
          type="password"
          value={qrToken}
          readOnly={isProcessing}
          onChange={(event) => setQrToken(event.target.value)}
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              event.preventDefault();
              processQrScan(event.currentTarget.value);
            }
          }}
          placeholder={
            isProcessing ? "Spracúvam QR scan..." : "Naskenuj QR kód čítačkou"
          }
          style={{
            border: error ? "1px solid red" : "1px solid #ccc",
            padding: "8px",
            marginRight: "10px",
          }}
        />
      </form>
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
