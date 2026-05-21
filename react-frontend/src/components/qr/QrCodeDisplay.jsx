import { QRCodeCanvas } from "qrcode.react";

export default function QrCodeDisplay({ qrToken }) {
  if (!qrToken) {
    return null;
  }

  return (
    <div className="text-center mt-3">
      <QRCodeCanvas value={qrToken} size={250} />
    </div>
  );
}
