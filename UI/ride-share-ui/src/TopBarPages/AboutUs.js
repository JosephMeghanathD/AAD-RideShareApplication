import { motion } from "framer-motion";

const AboutUs = () => {
  const portfolioUrl = "https://josephdanthikolla.netlify.app/";

  return (
    <div className="w-full h-full">
      <motion.iframe
        src={portfolioUrl}
        title="Joseph Danthikolla's Portfolio"
        className="w-full h-full border-0"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.5 }}
      />
    </div>
  );
};

export default AboutUs;
