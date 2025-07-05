import React, { useState } from 'react';
import './ContactUs.css'; // Import the new stylesheet

const ContactUs = () => {
	const [formData, setFormData] = useState({
		name: '',
		email: '',
		subject: '',
		message: '',
	});

	const [errors, setErrors] = useState({});
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [responseMessage, setResponseMessage] = useState('');
	const [messageType, setMessageType] = useState(''); // 'success' or 'error'

	const API_URL = 'https://auth-service-1002278726079.us-central1.run.app/api/rs/contact';

	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData((prevData) => ({
			...prevData,
			[name]: value,
		}));
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
		setResponseMessage('');
		setMessageType('');

		try {
			const response = await fetch(API_URL, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(formData),
			});

			if (response.ok) {
				const successText = await response.text();
				setResponseMessage(successText);
				setMessageType('success');
				setFormData({ name: '', email: '', subject: '', message: '' });
			} else {
				if (response.status === 400) {
					const errorData = await response.json();
					setErrors(errorData);
					setResponseMessage('Please correct the errors below.');
				} else {
					const errorText = await response.text();
					setResponseMessage(errorText || 'An unexpected server error occurred.');
				}
				setMessageType('error');
			}
		} catch (error) {
			console.error('Network or submission error:', error);
			setResponseMessage('Could not connect to the server. Please try again later.');
			setMessageType('error');
		} finally {
			setIsSubmitting(false);
		}
	};

	return (
		<div className="contact-form-container">
			<form className="contact-form" onSubmit={handleSubmit} noValidate>
				<fieldset>
					<legend>Contact Us</legend>

					<div className="form-group">
						<label htmlFor="name">Full Name</label>
						<input
							type="text"
							id="name"
							name="name"
							value={formData.name}
							onChange={handleChange}
							placeholder="e.g., John Doe"
							required
						/>
						{errors.name && <p className="error-text">{errors.name}</p>}
					</div>

					<div className="form-group">
						<label htmlFor="email">Email Address</label>
						<input
							type="email"
							id="email"
							name="email"
							value={formData.email}
							onChange={handleChange}
							placeholder="e.g., john.doe@example.com"
							required
						/>
						{errors.email && <p className="error-text">{errors.email}</p>}
					</div>

					<div className="form-group">
						<label htmlFor="subject">Subject</label>
						<input
							type="text"
							id="subject"
							name="subject"
							value={formData.subject}
							onChange={handleChange}
							placeholder="What is this about?"
							required
						/>
						{errors.subject && <p className="error-text">{errors.subject}</p>}
					</div>

					<div className="form-group">
						<label htmlFor="message">Message</label>
						<textarea
							id="message"
							name="message"
							value={formData.message}
							onChange={handleChange}
							placeholder="Please write your message here..."
							required
						/>
						{errors.message && <p className="error-text">{errors.message}</p>}
					</div>

					<button type="submit" className="submit-btn" disabled={isSubmitting}>
						{isSubmitting ? 'Sending...' : 'Send Message'}
					</button>
				</fieldset>
				
				{responseMessage && (
					<div className={`response-message ${messageType}`}>
						{responseMessage}
					</div>
				)}
			</form>
		</div>
	);
};

export default ContactUs;