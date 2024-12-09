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
} from "recharts";

const ChartCompose = ({
  data,
  lineDataKey,
  barDataKey,
  XAxisDataKey,
  barName,
  lineName,
  formatter,
}) => {
  return (
    <div className="flex flex-col items-center w-full relative -top-6 h-[500px]">
      <section>
        <h2 className="text-4xl text-mainSoil text-center my-6  font-extrabold">
          Revenue
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
