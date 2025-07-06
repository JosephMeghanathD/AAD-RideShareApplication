import { motion } from "framer-motion";
import HomePageText from "./HomePage";
import RideList from "./RidesData/RideList";

const Home = () => {
  return (
    <div className="bg-slate-900 min-h-full">
      <div className="w-full px-4 sm:px-6 lg:px-8 py-12">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, ease: "easeOut" }}
          className="mb-12"
        >
          <HomePageText />
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2, ease: "easeOut" }}
        >
          <RideList />
        </motion.div>
      </div>
    </div>
  );
};

export default Home;
