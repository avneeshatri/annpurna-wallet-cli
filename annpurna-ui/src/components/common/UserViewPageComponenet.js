import { Component } from "react";
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import { isUndefined, hasCookie, getCookie, displayCustomPopup } from './CustomUtilities'
import { getUserWalletSecret, setUserWalletSecret, balanceOf, hasUserWalletSecret, userWalletHistory, transfer } from './WalletService'


class UserViewPageComponenet extends Component {
    constructor() {
        super();
        this.state = {
            isOpen: false,
            balance: null,
            history: []
        };
        this.historyOfUserWallet = this.historyOfUserWallet.bind(this)
        this.balanceOfUserWallet = this.balanceOfUserWallet.bind(this)
    }

    handleOpen = () => {
        this.setState({ isOpen: true });
    }

    handleClose = () => {
        this.setState({ isOpen: false });
        const walletSecret = document.querySelector("#walletSecret").value;
        if (walletSecret) {
            setUserWalletSecret(walletSecret)
        } else {
            var response = {
                msg: "Wallet secret cannot be blank"
            }

            displayCustomPopup(response, "error")
        }
    }

    transferFromUserToUserWallet() {

        if (hasUserWalletSecret()) {

            const senderWalletSecret = getUserWalletSecret()
            const recipientWallet = document.querySelector("#recipientUserWallet").value;
            const amount = document.querySelector("#senderTransferAmount").value;
            transfer(senderWalletSecret, recipientWallet, amount, response => {
                console.log(response)

            })
        } else {
            var response = {
                msg: "Wallet secret not added in cookies"
            }

            displayCustomPopup(response, "error")
        }
    }

    balanceOfUserWallet() {
        const senderWalletSecret = getUserWalletSecret()
        if (hasUserWalletSecret()) {
            balanceOf(senderWalletSecret, response => {
                console.log(response)
                this.setState({ balance: response })
            })
        } else {
            var response = {
                msg: "Wallet secret not added in cookies"
            }

            displayCustomPopup(response, "error")
        }
    }

    historyOfUserWallet() {
        const senderWalletSecret = getUserWalletSecret()
        if (hasUserWalletSecret()) {
            userWalletHistory(senderWalletSecret, response => {
                
                this.setState({ history: response })
                console.log("History:"+this.state.history)
            })

        } else {
            var response = {
                msg: "Wallet secret not added in cookies"
            }

            displayCustomPopup(response, "error")
        }
    }

    render() {
        return (

            <div className='container'>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Add User Wallet Private Key</h3>
                <label>User provides its private key for his wallet which will be used for performing transactions.</label>
                <div>
                    <Popup trigger={<button className="btn btn-info btn-sm"> Click to add secret key</button>}
                        position="right center"
                        open={this.state.isOpen}
                        onOpen={this.handleOpen}
                    >
                        <div className="content">
                            <div className="col-lg-1">
                                <input type="text" id="walletSecret" placeholder='Enter Private Key' size="12"></input>
                            </div>
                            <div className="col-sm">
                                <input type="button" className="btn btn-success btn-sm"
                                    value='Add Key to cookies' onClick={this.handleClose} />
                            </div>
                        </div>
                    </Popup>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Transfer funds to wallet</h3>
                <label>Funds will be transfered from user wallet to recipient wallet</label>
                <div className="row">
                    <div className="col-lg-4">
                        <input type="text" id="recipientUserWallet" placeholder="Recipient User Wallet ID"></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="text" id="senderTransferAmount" placeholder="Amount"></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="button" className="btn btn-info  btn-sm"
                            value='Transfer' onClick={(e) => this.transferFromUserToUserWallet()} />
                    </div>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Wallet Balance</h3>
                <label>Get user wallet balance</label>
                <div className="row">
                    <div className="col-sm">
                        <input type="button" className="btn btn-info"
                            value='Get Balance' onClick={(e) => this.balanceOfUserWallet()} />
                        <span>
                            <label>{this.state.balance}</label>
                        </span>
                    </div>

                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Wallet History</h3>
                <label>History of all Updaets made to user wallet</label>
                <div className="row">
                    <div className="col-sm">
                        <input type="button" className="btn btn-info"
                            value='Get History' onClick={(e) => this.historyOfUserWallet()} />

                    </div>


                    <div className="grid container ">
                        <table className="table table-striped table-hover">
                            <thead className="th">
                                <tr>

                                    <td> Sno </td>
                                    <td> Amount </td>
                                </tr>
                            </thead>
                            <tbody className="tbody">
                                {
                                   
                                    this.state.history.map((item, i) => {
                                        
                                        return (
                                            <tr key={i}>
                                                <td key={i+10}>
                                                    {i+1}
                                                </td >
                                                <td key={i+20}>
                                                    
                                                    {
                                                    item.balance
                                                    }
                                                </td>
                                            </tr>
                                        )                                        
                                    }
                                    )
                                }
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        );

    }
}

export default UserViewPageComponenet;