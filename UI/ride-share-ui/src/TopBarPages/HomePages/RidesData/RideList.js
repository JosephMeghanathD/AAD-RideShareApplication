import { useEffect, useState } from "react";
import axios from "axios";
import { RideTable } from "./RideListTable";

const RideList = ({ forUser }) => {
  const [sortBy, setSortBy] = useState("postedAt");
  const [sortOrder, setSortOrder] = useState("asc");
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(15);
  const [error, setError] = useState(null);
  const [pageData, setPageData] = useState({
    content: [],
    totalPages: 0,
    totalElements: 0,
  });
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      const calculatedSize = Math.floor(window.innerHeight / 60);
      setPageSize(Math.max(10, calculatedSize));
    };

    handleResize();
    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      setError(null);

      let baseUrl =
        "https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/rides";
      if (forUser) {
        baseUrl =
          "https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/by/user";
      }

      const params = {
        page: currentPage - 1,
        size: pageSize,
        sortBy: sortBy,
        sortOrder: sortOrder,
      };

      try {
        const response = await axios.get(baseUrl, {
          params,
          headers: {
            Authorization: localStorage.getItem("jwtToken") || "XXX",
          },
        });
        setPageData(response.data);
      } catch (error) {
        if (error.response?.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        setError("Error fetching rides data. Please try again later.");
        console.error("Error fetching rides data:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [forUser, currentPage, sortBy, sortOrder, pageSize]);

  const handleSort = (key) => {
    if (sortBy === key) {
      setSortOrder((prevOrder) => (prevOrder === "asc" ? "desc" : "asc"));
    } else {
      setSortBy(key);
      setSortOrder("asc");
    }
    setCurrentPage(1);
  };

  const handlePageChange = (pageNumber) => {
    if (pageNumber >= 1 && pageNumber <= pageData.totalPages) {
      setCurrentPage(pageNumber);
    }
  };

  return (
    <RideTable
      rides={pageData.content}
      handleSort={handleSort}
      sortBy={sortBy}
      sortOrder={sortOrder}
      handlePageChange={handlePageChange}
      currentPage={currentPage}
      totalPages={pageData.totalPages}
      error={error}
      isLoading={isLoading}
    />
  );
};

export default RideList;
