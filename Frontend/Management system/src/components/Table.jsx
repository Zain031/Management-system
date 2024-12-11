const Table = ({
  arrayData = [],
  page = 1,
  tableHeader = [],
  notFoundMessage = "No data found",
  customRender = {}, // Tambahkan properti customRender untuk styling custom.
  excludeColumns = [], // Tambahkan properti excludeColumns untuk pengecualian render.
  renderActions = () => {},
}) => {
  const renderTableHeader = () =>
    tableHeader
      .filter((item) => !excludeColumns.includes(item.toLowerCase())) // Filter header berdasarkan excludeColumns
      .map((item, index) => <th key={index}>{item}</th>);

  const renderTableRow = (item, index) => (
    <tr key={index}>
      <th>{(page - 1) * 10 + (index + 1)}</th>
      {Object.entries(item)
        .filter(([key]) => !excludeColumns.includes(key))
        .map(([key, value]) => (
          <td key={key}>
            {customRender[key] ? customRender[key](value, item) : value}
          </td>
        ))}
      {renderActions(item)}
    </tr>
  );

  return (
    <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md mt-2">
      <table className="table">
        <thead>
          <tr>
            <th></th>
            {renderTableHeader()}
          </tr>
        </thead>
        <tbody>
          {Array.isArray(arrayData) && arrayData.length > 0 ? (
            arrayData.map(renderTableRow)
          ) : (
            <tr>
              <td colSpan={tableHeader.length + 1} className="text-center">
                {notFoundMessage}
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Table;
