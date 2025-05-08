'use strict';

// tag::vars[]
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
// end::vars[]

// tag::app[]
class App extends React.Component { // <1>

	constructor(props) {
		super(props);
		this.state = {employees: []};
	}

	componentDidMount() { // <2>
		client({method: 'GET', path: '/api/employees'}).done(response => {
			this.setState({employees: response.entity._embedded.employees});
		});
	}

	render() { // <3>
		return (
			<EmployeeList employees={this.state.employees}/>
		)
	}
}
// end::app[]

// tag::employee-list[]
class EmployeeList extends React.Component{
	render() {
		const tableStyle = { borderCollapse: 'collapse', width: '100%' };
		const cellStyle = { border: '1px solid black', padding: '8px', textAlign: 'left' };

		const employees = this.props.employees.map(employee =>
			<Employee key={employee._links.self.href} employee={employee}/>
		);
		return (
			<table style={tableStyle}>
				<tbody>
					<tr>
						<th style={cellStyle}> First Name</th>
						<th style={cellStyle} >Last Name</th>
						<th style={cellStyle} >Description</th>
						<th style={cellStyle} >Job Years</th>
						<th style={cellStyle} >Job Title</th>
						<th style={cellStyle} >E-mail</th>
					</tr>
					{employees}
				</tbody>
			</table>
		)
	}
}
// end::employee-list[]

// tag::employee[]
class Employee extends React.Component{
	render() {
		const { employee, cellStyle } = this.props;

		return (
			<tr>
				<td style={cellStyle}>{employee.firstName}</td>
				<td style={cellStyle}>{employee.lastName}</td>
				<td style={cellStyle}>{employee.description}</td>
				<td style={cellStyle}>{employee.jobYears}</td>
				<td style={cellStyle}>{employee.jobTitle}</td>
				<td style={cellStyle}>{employee.email}</td>
			</tr>
		)
	}
}
// end::employee[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]
