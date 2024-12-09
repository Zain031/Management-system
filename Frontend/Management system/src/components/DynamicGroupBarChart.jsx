import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";

const DynamicGroupBarChart = ({
  data,
  chartTitle,
  XAxisDataKey,
  formatter = (value) => value,
  bars = [],
}) => {
  return (
    <div className="flex flex-col items-center w-full relative -top-6 h-[500px]">
      <section>
        <h2 className="text-4xl text-mainSoil text-center my-6 font-extrabold">
          {chartTitle}
        </h2>
      </section>
      <ResponsiveContainer width="100%" height={400}>
        <BarChart
          data={data}
          margin={{
            top: 20,
            right: 20,
            bottom: 20,
            left: 20,
          }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey={XAxisDataKey} />
          <YAxis />
          <Tooltip formatter={formatter} />
          <Legend />
          {/* Dynamically render bars */}
          {bars.map(({ dataKey, name, fill, stackId }, index) => (
            <Bar
              key={`bar-${index}`}
              dataKey={dataKey}
              name={name}
              barSize={40}
              stackId={stackId || undefined}
              fill={fill}
            />
          ))}
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default DynamicGroupBarChart;
