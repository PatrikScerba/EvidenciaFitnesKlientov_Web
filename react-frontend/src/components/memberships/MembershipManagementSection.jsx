import { useEffect, useState } from "react";
import {createOrExtendMembership,getMembershipByClientId} from "../../api/membershipApi";

export default function MembershipManagementSection({ client, onClose }) {
  const [duration, setDuration] = useState("DAYS_30");
  const [membership, setMembership] = useState(null);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [loadingMembership, setLoadingMembership] = useState(true);
  const isActive = membership?.status === "ACTIVE";

  useEffect(() => {
    async function fetchMembership() {
      setLoadingMembership(true);
      setError("");

      try {
        const data = await getMembershipByClientId(client.clientId);
        setMembership(data);
      } catch{
        setMembership(null);
      } finally {
        setLoadingMembership(false);
      }
    }

    fetchMembership();
  }, [client.clientId]);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setResult(null);
    setLoading(true);

    try {
      const membershipData = {
        clientId: client.clientId,
        duration: duration,
      };

      const response = await createOrExtendMembership(membershipData);

      setResult(response);
      setMembership(response);
    } catch (err) {
      setError(err.message || "Nepodarilo sa uložiť permanentku.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{
        border: "1px solid #ccc",
        padding: "40px",
        marginTop: "20px",
      }}
    >
      <h3>Správa permanentky</h3>

      <p>
        <strong>ID klienta:</strong> {client.clientId}
      </p>

      <p>
        <strong>Klient:</strong> {client.firstName} {client.lastName}
      </p>
      <p>
        <strong>Email:</strong> {client.email}
      </p>

      <div
        style={{
          marginTop: "20px",
          marginBottom: "20px",
          padding: "15px",
          border: "1px solid #ddd",
          backgroundColor: "#f8f8f8",
        }}
      >
        <h4>Aktuálny stav permanentky</h4>

        {loadingMembership ? (
          <p>Načítavam stav permanentky...</p>
        ) : membership ? (
          <>
            <p>
              <strong>Status:</strong> {membership.status}
            </p>
            <p>
              <strong>Platná od:</strong> {membership.validFrom}
            </p>
            <p>
              <strong>Platná do:</strong> {membership.validTo}
            </p>
          </>
        ) : (
          <p>Klient zatiaľ nemá žiadnu permanentku.</p>
        )}
      </div>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "15px" }}>
          <label htmlFor="duration">Dĺžka permanentky:</label>
          <br />
          <select
            id="duration"
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
          >
            <option value="DAYS_30">30 dní</option>
            <option value="DAYS_90">90 dní</option>
            <option value="DAYS_180">180 dní</option>
            <option value="DAYS_365">365 dní</option>
          </select>
        </div>

        <button type="submit" disabled={loading || isActive}>
          {loading ? "Ukladám..." : "Vytvoriť / predĺžiť permanentku"}
        </button>

        <button type="button" onClick={onClose} style={{ marginLeft: "10px" }}>
          Zrušiť
        </button>
      </form>

      {membership?.status === "ACTIVE" && (
        <p style={{ color: "orange", marginTop: "10px" }}>
          Permanentka je aktuálne aktívna. Predĺženie bude možné až po vypršaní aktuálnej permanentky.
        </p>
      )}

      {result && (
        <div style={{ marginTop: "20px" }}>
          <h4>Výsledok poslednej operácie</h4>
          <p>
            <strong>Status:</strong> {result.status}
          </p>
          <p>
            <strong>Platná od:</strong> {result.validFrom}
          </p>
          <p>
            <strong>Platná do:</strong> {result.validTo}
          </p>
        </div>
      )}
    </div>
  );
}
