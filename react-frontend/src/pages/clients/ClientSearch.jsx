import { useState } from "react";

export default function ClientSearch({ onSearch, loading }) {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
  });

  function handleChange(e) {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    onSearch(form);
  }

  function handleReset() {
    const emptyForm = {
      firstName: "",
      lastName: "",
      email: "",
    };
    onSearch(null);
  }

  return (
    <div style={{ marginBottom: "20px" }}>
      <h3>Vyhľadávanie klientov</h3>

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
          {loading ? "Vyhľadávam..." : "Vyhľadať"}
        </button>

        <button type="button" onClick={handleReset} disabled={loading}>
          Reset
        </button>
      </form>
    </div>
  );
}
