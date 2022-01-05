import React, { Component } from 'react'
import { isUndefined, displayCustomPopup } from './CustomUtilities'
import { createWallet, readWallet, createPartnerWallet, addFunds, transferTo, transfer } from './WalletService'



class ZudexoPageComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user_wallet: null,
            user_wallet_secret: null,
            balance: null
        };

        this.createUserWallet = this.createUserWallet.bind(this);
        this.createOrgWallet = this.createOrgWallet.bind(this);

    }

    createOrgWallet() {
        const orgMspIdInput = document.querySelector("#orgMspId");
        const orgMspIdVal = orgMspIdInput.value
        console.log()
        if (!isUndefined(orgMspIdVal)) {
            createPartnerWallet(orgMspIdVal, response => {
                this.setState({
                    user_wallet: response.id,
                })
            })
        } else {
            var popupMsg = { msg: "Org Msp ID is empty", detailedMsg: "Org msp id is cannot be empty" }
            displayCustomPopup(popupMsg, "error")
        }
    }

    createUserWallet() {
        createWallet(response => {
            this.setState({
                user_wallet: response.id,
                user_wallet_secret: response.secret
            })
        })
    }

    transferToUserWallet() {
        const userWallet = document.querySelector("#transferToWallet").value;
        const amount = document.querySelector("#transferAmount").value;
        transferTo(userWallet, amount, response => {
            console.log(response)
        })
    }

    addFundsToZudexo() {
        const fund = document.querySelector("#fundAmount").value;
        addFunds(fund, response => {
            console.log("funds added")
        })
    }


    transferToUserWallet() {
        const userWallet = document.querySelector("#transferToWallet").value;
        const amount = document.querySelector("#transferAmount").value;
        transferTo(userWallet, amount, response => {
            console.log(response)

        })
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
                <h3>Create Annpurna Network Partner wallets</h3>
                <label>Zudexo is authorized to create wallet for network members.
                    Network members can use wallet to receive funds from user when they purchase any food item from their Online store.</label>
                <div className="row">
                    <div className="col-lg-4">
                        <input type="text" id="orgMspId" placeholder="Provide Org MspID"></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="button" className="btn btn-info  btn-sm"
                            value='Create Org Wallet' onClick={(e) => this.createOrgWallet()} />
                    </div>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Add Funds to Zudexo Wallet</h3>
                <div className="row">
                    <label>Zudexo can add funds to its own wallet. Zudexo wallet is primary wallet in the network.</label>
                    <div className="col-lg-4">
                        <input type="text" id="fundAmount" placeholder="Enter Amount"></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="button" className="btn btn-info btn-sm"
                            value='Add Funds' onClick={(e) => this.addFundsToZudexo()} />
                    </div>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Create User Wallet</h3>
                <div>
                    <label className="label">Create user wallet which can be used on network to buy items</label>
                    <div className="row">
                        <input type="button" className="btn btn-info btn-sm"
                            value='Create Wallet' onClick={(e) => this.createUserWallet()} />
                    </div>
                </div>
                <div className="row">
                    <label>User Wallet Id :</label>
                    <div className="col-sm">
                        {this.state.user_wallet}
                    </div>
                </div>
                <div className="row">
                    <label>User Wallet Secret: </label>
                    <div className="col-sm">
                        {this.state.user_wallet_secret}
                    </div>
                </div>
                <hr style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor: '#000000'
                }} />
                <h3>Transfer to Wallet</h3>
                <div className="row">
                    <label>Transfer to wallet of user or wallet of Network member. Wallet Id of Network member is msp id. Amount will be deducted from Zudexo Wallet and credited to provided wallet id</label>
                    <div className="col-lg-4">
                        <input type="text" id="transferToWallet" placeholder='Wallet ID'></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="text" id="transferAmount" placeholder='Enter Amount'></input>
                    </div>
                    <div className="col-lg-4">
                        <input type="button" className="btn btn-info btn-sm"
                            value='Transfer' onClick={(e) => this.transferToUserWallet()} />
                    </div>
                </div>

            </div>
        );
    }
}

export default ZudexoPageComponent;
