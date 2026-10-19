import { useRef, useState } from "react";

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
  };
}
