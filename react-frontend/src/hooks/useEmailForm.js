import { useRef, useState } from "react";
import { sendEmail } from "../api/emailApi";

export default function useEmailForm() {
  const [emailData, setEmailData] = useState({
    subject: "",
    message: "",
  });

  const [attachments, setAttachments] = useState([]);
  const [error, setError] = useState("");
  const [isSending, setIsSending] = useState(false);

  const attachmentInputRef = useRef(null);

  const handleAttachmentsChange = (event) => {
    const selectedFiles = Array.from(event.target.files ?? []);

    setAttachments(selectedFiles);
  };

  const handleSubmit = async (clientId) => {
    setIsSending(true);
    setError("");

    try {
      const request = {
        subject: emailData.subject,
        message: emailData.message,
        sendToAll: false,
        clientIds: [clientId],
      };

      await sendEmail(request, attachments);
    } catch (err) {
      setError(err.message || "Email sa nepodarilo odoslať.");
    } finally {
      setIsSending(false);
    }
  };

  const handleChange = (event) => {
    const { name, value } = event.target;

    setEmailData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return {
    emailData,
    attachments,
    error,
    isSending,
    attachmentInputRef,
    handleChange,
    handleAttachmentsChange,
    handleSubmit,
  };
}
