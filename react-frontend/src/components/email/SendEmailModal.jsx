import useEmailForm from "../../hooks/useEmailForm";

export default function SendEmailModal({ client, onClose }) {
  const {
    emailData,
    error,
    isSending,
    handleChange,
    attachmentInputRef,
    handleAttachmentsChange,
  } = useEmailForm();

  return (
    <div style={{ padding: "20px", border: "1px solid #ccc" }}>
      <h3>Odoslať email klientovi</h3>

      <p>
        Príjemca: {client.firstName} {client.lastName} ({client.email})
      </p>

      <form>
        <div>
          <label>Predmet</label>
          <input
            type="text"
            name="subject"
            value={emailData.subject}
            onChange={handleChange}
            maxLength={150}
            required
          />
        </div>

        <div>
          <label>Správa</label>
          <textarea
            name="message"
            value={emailData.message}
            onChange={handleChange}
            maxLength={5000}
            rows={6}
          />
        </div>

        <div>
          <label>Prílohy</label>
          <input
            type="file"
            multiple
            ref={attachmentInputRef}
            onChange={handleAttachmentsChange}
          />
        </div>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <button type="button" onClick={onClose} disabled={isSending}>
          Zrušiť
        </button>
      </form>
    </div>
  );
}
