import {
  ResponsiveContainer,
  ComposedChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  Line,
  Area,
} from "recharts";

const ChartCompose = ({
  data,
  lineDataKey,
  barDataKey,
  areaDataKey,
  areaName,
  XAxisDataKey,
  barName,
  lineName,
  formatter,
  chartTitle,
}) => {
  return (
    <div className="flex flex-col items-center w-full relative -top-6 h-[500px]">
      <section>
        <h2 className="text-4xl text-mainSoil text-center my-6  font-extrabold">
          {chartTitle}
        </h2>
      </section>
      <ResponsiveContainer width="100%" height={400}>
        <ComposedChart
          data={data}
          margin={{
            top: 20,
            right: 20,
            bottom: 20,
            left: 20,
          }}>
          <CartesianGrid stroke="#f5f5f5" />
          <XAxis dataKey={XAxisDataKey || "name"} />
          <YAxis />
          <Tooltip
            formatter={(value) => {
              return formatter(value);
            }}
          />
          <Legend />
          {areaDataKey && (
            <Area
              type="monotone"
              dataKey={areaDataKey}
              name={areaName}
              fill="#8884d8"
              stroke="#8884d8"
            />
          )}
          {barDataKey && (
            <Bar
              dataKey={`${barDataKey}`}
              name={barName}
              barSize={40}
              style={{ borderRadius: "100px" }}
              fill="#503a3a"
            />
          )}
          {lineDataKey && (
            <Line
              type="monotone"
              dataKey={`${lineDataKey}`}
              name={lineName}
              stroke="#503a3a"
            />
          )}
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ChartCompose;
