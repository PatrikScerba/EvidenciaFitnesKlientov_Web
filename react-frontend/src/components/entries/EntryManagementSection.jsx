import { useState } from "react";
import { createEntry, registerDeparture } from "../../api/entryApi";

export default function EntryManagementSection({ client, onClose }) {
  const [note, setNote] = useState("");
  const [entryResult, setEntryResult] = useState(null);
  const [error, setError] = useState("");
  const [loadingEntry, setLoadingEntry] = useState(false);
  const [loadingDeparture, setLoadingDeparture] = useState(false);

  function formatReason(reason) {
    switch (reason) {
      case "OK":
        return "Vstup povolený";
      case "NO_MEMBERSHIP":
        return "Klient nemá permanentku";
      case "MEMBERSHIP_EXPIRED":
        return "Platnosť permanentky vypršala";
      case "ALREADY_ENTERED_TODAY":
        return "Klient už dnes mal vstup";
      case "AGE_RESTRICTION":
        return "Klient nespĺňa vekové obmedzenie";
      default:
        return reason;
    }
  }

  async function handleCreateEntry() {
    const confirmed = window.confirm(
      `Naozaj chcete zaevidovať vstup klienta ${client.firstName} ${client.lastName}?`
    );

    if (!confirmed) {
      return;
    }
    setError("");
    setEntryResult(null);
    setLoadingEntry(true);

    try {
      const response = await createEntry({
        clientId: client.clientId,
        note: note,
      });

      setEntryResult(response);
      setNote("");
    } catch (err) {
      setError(err.message || "Nepodarilo sa zaevidovať vstup.");
    } finally {
      setLoadingEntry(false);
    }
  }

  async function handleRegisterDeparture() {
    const confirmed = window.confirm(
      `Naozaj chcete zaznamenať odchod klienta ${client.firstName} ${client.lastName}?`
    );

    if (!confirmed) {
      return;
    }

    setError("");
    setLoadingDeparture(true);

    try {
      const response = await registerDeparture(client.clientId);
      setEntryResult(response);
    } catch (err) {
      setError(err.message || "Nepodarilo sa zaznamenať odchod.");
    } finally {
      setLoadingDeparture(false);
    }
  }

  return (
    <div
      style={{
        marginTop: "20px",
        padding: "20px",
        border: "1px solid #ccc",
        borderRadius: "8px",
      }}
    >
      <h3>Správa vstupu klienta</h3>

      <p>
        <strong>ID:</strong> {client.clientId}
      </p>
      <p>
        <strong>Meno:</strong> {client.firstName} {client.lastName}
      </p>
      <p>
        <strong>Email:</strong> {client.email}
      </p>

      <div style={{ marginTop: "15px" }}>
        <label>Poznámka:</label>
        <br />
        <textarea
          value={note}
          onChange={(e) => setNote(e.target.value)}
          rows="3"
          style={{ width: "100%", marginTop: "5px" }}
        />
      </div>

      <div style={{ marginTop: "15px" }}>
        <button onClick={handleCreateEntry} disabled={loadingEntry}>
          {loadingEntry ? "Ukladám vstup..." : "Zaevidovať vstup"}
        </button>

        {entryResult?.status === "APPROVED" && !entryResult.departureTime && (
          <button
            onClick={handleRegisterDeparture}
            disabled={loadingDeparture}
            style={{ marginLeft: "10px" }}
          >
            {loadingDeparture ? "Ukladám odchod..." : "Zaznamenať odchod"}
          </button>
        )}

        <button onClick={onClose} style={{ marginLeft: "10px" }}>
          Zavrieť
        </button>
      </div>

      {error && <p style={{ color: "red", marginTop: "15px" }}>{error}</p>}

      {entryResult && (
        <div style={{ marginTop: "20px" }}>
          <h4>Výsledok operácie</h4>

          <p
            style={{
              color: entryResult.status === "APPROVED" ? "green" : "red",
            }}
          >
            <strong>Status:</strong> {entryResult.status}
          </p>
          <p>
            <strong>Dôvod:</strong> {formatReason(entryResult.reason)}
          </p>
          <p>
            <strong>Dátum:</strong> {entryResult.date}
          </p>
          <p>
            <strong>Čas príchodu:</strong> {entryResult.arrivalTime}
          </p>

          {entryResult.departureTime && (
            <p>
              <strong>Čas odchodu:</strong> {entryResult.departureTime}
            </p>
          )}

          {entryResult.note && (
            <p>
              <strong>Poznámka:</strong> {entryResult.note}
            </p>
          )}
        </div>
      )}
    </div>
  );
}
