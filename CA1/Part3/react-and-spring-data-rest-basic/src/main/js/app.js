'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
require('./table-style.css'); // <--- importação do CSS
// end::vars[]

// tag::app[]
class App extends React.Component {
	constructor(props) {
		super(props);
		this.state = { employees: [] };
	}

	componentDidMount() {
		client({ method: 'GET', path: '/api/employees' }).done(response => {
			this.setState({ employees: response.entity._embedded.employees });
		});
	}

	render() {
		return (
			<EmployeeList employees={this.state.employees} />
		)
	}
}
// end::app[]

// tag::employee-list[]
class EmployeeList extends React.Component {
	render() {
		const employees = this.props.employees.map(employee =>
			<Employee key={employee._links.self.href} employee={employee} />
		);
		return (
			<table>
				<tbody>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Description</th>
					<th>Job Years</th>
					<th>Job Title</th>
					<th>E-mail</th>
				</tr>
				{employees}
				</tbody>
			</table>
		)
	}
}
// end::employee-list[]

// tag::employee[]
class Employee extends React.Component {
	render() {
		const { employee } = this.props;

		return (
			<tr>
				<td>{employee.firstName}</td>
				<td>{employee.lastName}</td>
				<td>{employee.description}</td>
				<td>{employee.jobYears}</td>
				<td>{employee.jobTitle}</td>
				<td>{employee.email}</td>
			</tr>
		)
	}
}
// end::employee[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
);
// end::render[]