import { useState } from "react";
import { updateClient } from "../../api/clientApi";

export default function ClientEdit({ client, onCancel, onUpdated }) {
  const [form, setForm] = useState({
    firstName: client.firstName || "",
    lastName: client.lastName || "",
    dateOfBirth: client.dateOfBirth || "",
    phoneNumber: client.phoneNumber || "",
    address: client.address || "",
    email: client.email || "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  function handleChange(e) {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const updatedClient = await updateClient(client.clientId, form);

      if (onUpdated) {
        onUpdated(updatedClient);
      }
    } catch (err) {
      setError(err.message || "Úprava klienta zlyhala.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{ marginTop: "20px", padding: "20px", border: "1px solid #ccc" }}
    >
      <h3>Úprava klienta</h3>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "10px" }}>
          <input
            type="text"
            name="firstName"
            placeholder="Meno"
            value={form.firstName}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <input
            type="text"
            name="lastName"
            placeholder="Priezvisko"
            value={form.lastName}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <input
            type="date"
            name="dateOfBirth"
            value={form.dateOfBirth}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <input
            type="text"
            name="phoneNumber"
            placeholder="Telefónne číslo"
            value={form.phoneNumber}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <input
            type="text"
            name="address"
            placeholder="Adresa"
            value={form.address}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          style={{ marginRight: "10px" }}
        >
          {loading ? "Ukladám..." : "Uložiť zmeny"}
        </button>

        <button type="button" onClick={onCancel} disabled={loading}>
          Zrušiť
        </button>
      </form>

      {error && <p style={{ color: "red", marginTop: "10px" }}>{error}</p>}
    </div>
  );
}
