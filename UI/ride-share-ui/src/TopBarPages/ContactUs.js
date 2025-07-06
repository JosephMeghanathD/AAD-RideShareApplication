import React, { useState } from "react";

const FormInput = ({
  id,
  name,
  type = "text",
  value,
  onChange,
  placeholder,
  error,
}) => (
  <div>
    <label
      htmlFor={id}
      className="block mb-2 text-sm font-medium text-slate-300"
    >
      {placeholder}
    </label>
    <input
      type={type}
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={`e.g., ${placeholder}...`}
      required
      className={`w-full bg-slate-700 text-slate-100 border rounded-lg p-3 focus:outline-none focus:ring-2 placeholder-slate-400 transition-colors ${
        error
          ? "border-red-500 focus:ring-red-500"
          : "border-transparent focus:ring-blue-500"
      }`}
    />
    {error && <p className="mt-2 text-sm text-red-400">{error}</p>}
  </div>
);

const FormTextarea = ({ id, name, value, onChange, placeholder, error }) => (
  <div>
    <label
      htmlFor={id}
      className="block mb-2 text-sm font-medium text-slate-300"
    >
      {placeholder}
    </label>
    <textarea
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      placeholder="Please write your message here..."
      rows="5"
      required
      className={`w-full bg-slate-700 text-slate-100 border rounded-lg p-3 focus:outline-none focus:ring-2 placeholder-slate-400 transition-colors ${
        error
          ? "border-red-500 focus:ring-red-500"
          : "border-transparent focus:ring-blue-500"
      }`}
    />
    {error && <p className="mt-2 text-sm text-red-400">{error}</p>}
  </div>
);

const InButtonSpinner = () => (
  <svg
    className="animate-spin h-5 w-5 text-white"
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
  >
    <circle
      className="opacity-25"
      cx="12"
      cy="12"
      r="10"
      stroke="currentColor"
      strokeWidth="4"
    ></circle>
    <path
      className="opacity-75"
      fill="currentColor"
      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
    ></path>
  </svg>
);

const ContactUs = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [responseMessage, setResponseMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  const API_URL =
    "https://auth-service-1002278726079.us-central1.run.app/api/rs/contact";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
    if (errors[name]) {
      setErrors((prevErrors) => {
        const newErrors = { ...prevErrors };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setErrors({});
    setResponseMessage("");
    setMessageType("");

    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const successText = await response.text();
        setResponseMessage(successText);
        setMessageType("success");
        setFormData({ name: "", email: "", subject: "", message: "" });
      } else {
        if (response.status === 400) {
          const errorData = await response.json();
          setErrors(errorData);
          setResponseMessage("Please correct the errors marked below.");
        } else {
          const errorText = await response.text();
          setResponseMessage(
            errorText || "An unexpected server error occurred."
          );
        }
        setMessageType("error");
      }
    } catch (error) {
      console.error("Network or submission error:", error);
      setResponseMessage(
        "Could not connect to the server. Please try again later."
      );
      setMessageType("error");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="bg-slate-900 min-h-screen flex items-center justify-center p-4">
      <div className="w-full max-w-2xl mx-auto">
        <form
          className="bg-slate-800 p-8 rounded-2xl shadow-xl space-y-6"
          onSubmit={handleSubmit}
          noValidate
        >
          <h2 className="text-3xl font-bold text-white text-center mb-6">
            Contact Us
          </h2>

          <FormInput
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Full Name"
            error={errors.name}
          />
          <FormInput
            id="email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Email Address"
            error={errors.email}
          />
          <FormInput
            id="subject"
            name="subject"
            value={formData.subject}
            onChange={handleChange}
            placeholder="Subject"
            error={errors.subject}
          />
          <FormTextarea
            id="message"
            name="message"
            value={formData.message}
            onChange={handleChange}
            placeholder="Message"
            error={errors.message}
          />

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full mt-2 flex items-center justify-center text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-3 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 disabled:opacity-50 disabled:cursor-wait"
          >
            {isSubmitting ? <InButtonSpinner /> : "Send Message"}
          </button>

          {responseMessage && (
            <div
              className={`p-4 mt-4 text-sm rounded-lg text-center ${
                messageType === "success"
                  ? "bg-green-800 text-green-200"
                  : "bg-red-800 text-red-200"
              }`}
            >
              {responseMessage}
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default ContactUs;
