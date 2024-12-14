import SelectMonthOrDate from "./SelectMonthOrDate";
import SelectMonth from "./SelectMonth";
import SelectYear from "./SelectYear";

const ExportModal = ({
  handleExport,
  exportPer,
  setExportPer,
  selectedMonth,
  setSelectedMonth,
  selectedYear,
  setSelectedYear,
  selectedDate,
  setSelectedDate,
}) => {
  return (
    <dialog id="export_modal" className="modal">
      <div className="modal-box">
        <form onSubmit={handleExport}>
          <SelectMonthOrDate
            exportPer={exportPer}
            setExportPer={setExportPer}
          />
          {!exportPer && (
            <p className="text-sm text-gray-500 mb-4">
              Default export per month
            </p>
          )}
          {exportPer === "month" && (
            <>
              <SelectMonth
                selectedMonth={selectedMonth}
                setSelectedMonth={setSelectedMonth}
              />
              <SelectYear
                selectedYear={selectedYear}
                setSelectedYear={setSelectedYear}
              />
            </>
          )}
          {exportPer === "date" && (
            <input
              type="date"
              className="input input-bordered input-ghost w-full my-2"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value.split("T")[0])}
            />
          )}
          <button className="btn btn-outline btn-primary w-full">Submit</button>
        </form>
        <div className="modal-action">
          <form method="dialog">
            <button className="btn">Close</button>
          </form>
        </div>
      </div>
    </dialog>
  );
};

export default ExportModal;
