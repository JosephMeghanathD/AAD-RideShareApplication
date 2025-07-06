import React from "react";
import ErrorToast from "../../../BasicElements/ErrorToast";
import RideListRow from "./RideListRow";

const SortIcon = ({ sortOrder }) => (
  <span className="opacity-70">{sortOrder === "asc" ? "▲" : "▼"}</span>
);

const PaginationButton = ({ onClick, disabled, children }) => (
  <button
    onClick={onClick}
    disabled={disabled}
    className="px-4 py-2 text-sm font-medium text-slate-300 bg-slate-800 border border-slate-700 rounded-md hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed"
  >
    {children}
  </button>
);

const Pagination = ({ currentPage, totalPages, onPageChange, isLoading }) => {
  if (totalPages <= 1) return null;

  return (
    <div className="flex items-center justify-between px-4 py-3 bg-slate-800 sm:px-6">
      <div className="flex-1 flex justify-start">
        <PaginationButton
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 1 || isLoading}
        >
          Previous
        </PaginationButton>
      </div>
      <div className="text-sm text-slate-400">
        Page <span className="font-medium text-slate-200">{currentPage}</span>{" "}
        of <span className="font-medium text-slate-200">{totalPages}</span>
      </div>
      <div className="flex-1 flex justify-end">
        <PaginationButton
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages || isLoading}
        >
          Next
        </PaginationButton>
      </div>
    </div>
  );
};

export const RideTable = ({
  rides,
  handleSort,
  sortBy,
  sortOrder,
  currentPage,
  totalPages,
  handlePageChange,
  error,
  isLoading,
}) => {
  const isLoggedIn = localStorage.getItem("jwtToken") !== null;
  const colSpan = isLoggedIn ? 8 : 5;

  const TableHeader = ({ sortKey, children }) => (
    <th
      scope="col"
      className="px-6 py-4 text-left text-xs font-medium text-slate-400 uppercase tracking-wider"
    >
      <div
        className="flex items-center gap-2 cursor-pointer select-none"
        onClick={() => handleSort(sortKey)}
      >
        {children}
        {sortBy === sortKey && <SortIcon sortOrder={sortOrder} />}
      </div>
    </th>
  );

  return (
    <div className="rounded-xl overflow-hidden bg-slate-800">
      <div className="overflow-x-auto">
        <table className="min-w-full">
          <thead className="bg-slate-800">
            <tr>
              <TableHeader sortKey="startingFromLocation">From</TableHeader>
              <TableHeader sortKey="destination">To</TableHeader>
              <TableHeader sortKey="numberOfPeople">Seats</TableHeader>
              <TableHeader sortKey="fare">Fare</TableHeader>
              <TableHeader sortKey="timeOfRide">Ride Time</TableHeader>
              {isLoggedIn && (
                <TableHeader sortKey="postedBy.name">Posted By</TableHeader>
              )}
              <TableHeader sortKey="postedAt">Posted At</TableHeader>
              {isLoggedIn && (
                <th
                  scope="col"
                  className="px-6 py-4 text-left text-xs font-medium text-slate-400 uppercase tracking-wider"
                >
                  Actions
                </th>
              )}
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-700">
            {isLoading ? (
              <tr>
                <td
                  colSpan={colSpan}
                  className="text-center py-10 text-slate-400"
                >
                  Loading rides...
                </td>
              </tr>
            ) : rides && rides.length > 0 ? (
              rides.map((ride) => <RideListRow key={ride.rideId} ride={ride} />)
            ) : (
              <tr>
                <td
                  colSpan={colSpan}
                  className="text-center py-10 text-slate-400"
                >
                  No rides found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
        isLoading={isLoading}
      />
      {error && <ErrorToast message={error} />}
    </div>
  );
};
