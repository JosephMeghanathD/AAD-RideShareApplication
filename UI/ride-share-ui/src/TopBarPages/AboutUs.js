import { motion } from 'framer-motion';

const AboutUs = () => {
  const portfolioUrl = 'https://josephdanthikolla.netlify.app/';

  return (
    // 'w-full h-full' tells this div to take up 100% of the width and height 
    // of its parent, which is the <main> tag in App.js.
    <div className="w-full h-full">
      <motion.iframe
        src={portfolioUrl}
        title="Joseph Danthikolla's Portfolio"
        className="w-full h-full border-0" // The iframe also fills its container
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.5 }}
      />
    </div>
  );
};

export default AboutUs;