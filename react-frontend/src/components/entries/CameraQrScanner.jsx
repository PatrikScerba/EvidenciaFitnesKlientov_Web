import { useEffect, useRef, useState } from "react";
import { Html5Qrcode } from "html5-qrcode";

export default function CameraQrScanner({ onScanSuccess, onScannerStop }) {
  const scannerRef = useRef(null);
  const isProcessingRef = useRef(false);

  const [scanning, setScanning] = useState(false);
  const [cameraError, setCameraError] = useState("");

  const scannerElementId = "qr-camera-reader";

  async function startScanner() {
    setCameraError("");

    if (scannerRef.current) {
      return;
    }

    try {
      const scanner = new Html5Qrcode(scannerElementId);
      scannerRef.current = scanner;

      await scanner.start(
        { facingMode: "environment" },
        {
          fps: 10,
          qrbox: { width: 300, height: 300 },
        },
        async (decodedText) => {
          if (isProcessingRef.current) {
            return;
          }

          isProcessingRef.current = true;

          try {
            await onScanSuccess(decodedText);
          } finally {
            setTimeout(() => {
              isProcessingRef.current = false;
            }, 1000);
          }
        }
      );

      setScanning(true);
    } catch (error) {
      setCameraError("Kameru sa nepodarilo spustiť.");
      console.error(error);
    }
  }

  async function stopScanner() {
    if (scannerRef.current) {
      try {
        await scannerRef.current.stop();
        await scannerRef.current.clear();
      } catch (error) {
        console.error(error);
      } finally {
        scannerRef.current = null;
        isProcessingRef.current = false;
        setScanning(false);
        onScannerStop?.();
      }
    }
  }

  useEffect(() => {
    return () => {
      stopScanner();
    };
  }, []);

  return (
    <div style={{ marginTop: "20px" }}>
      <h4>Scan cez kameru</h4>

      <div id={scannerElementId} style={{ width: "400px" }} />

      {!scanning ? (
        <button
          type="button"
          onClick={startScanner}
          style={{ marginTop: "12px" }}
        >
          Spustiť kameru
        </button>
      ) : (
        <button
          type="button"
          onClick={stopScanner}
          style={{ marginTop: "12px" }}
        >
          Zastaviť kameru
        </button>
      )}

      {cameraError && (
        <p style={{ color: "red", fontWeight: "bold" }}>{cameraError}</p>
      )}
    </div>
  );
}
