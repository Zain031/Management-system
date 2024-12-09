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

const MixBarChart = ({
  data,
  chartTitle,
  XAxisDataKey = "name",
  formatter = (value) => value,
  barGroupOne,
  barGroupTwo,
  barGroupOneBars = [],
  barGroupTwoBars = [],
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
          {/* Bar Group One */}
          {barGroupOne &&
            barGroupOneBars.map(({ dataKey, name, fill }, index) => (
              <Bar
                key={`barGroupOne-${index}`}
                dataKey={dataKey}
                name={name}
                barSize={40}
                stackId="a"
                fill={fill}
              />
            ))}
          {/* Bar Group Two */}
          {barGroupTwo &&
            barGroupTwoBars.map(({ dataKey, name, fill }, index) => (
              <Bar
                key={`barGroupTwo-${index}`}
                dataKey={dataKey}
                barSize={40}
                name={name}
                fill={fill}
              />
            ))}
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default MixBarChart;
