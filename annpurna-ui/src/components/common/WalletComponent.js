import React, { Component } from 'react'
import {isUndefined,displayCustomPopup} from './CustomUtilities'
import {createWallet,readWallet,createPartnerWallet,balanceOf,addFunds,transferTo,transfer} from './WalletService'



class WalletComponent extends Component {

  
    constructor(props) {
        super(props);
        this.state = {
            user_wallet : null,
            user_wallet_secret : null,
            balance : null
        };

        this.createUserWallet = this.createUserWallet.bind(this);
        this.readUserWallet = this.readUserWallet.bind(this)
        this.createOrgWallet = this.createOrgWallet.bind(this)
        this.balanceOfUserWallet = this.balanceOfUserWallet.bind(this)
    }


    createUserWallet(){
        createWallet(response => {
            this.setState({
                user_wallet : response.id,
                user_wallet_secret : response.secret 
            })
        })
    }


    readUserWallet(){
        readWallet(this.state.user_wallet_secret , response =>{
            console.log(response)
            this.setState({balance:response.balance})
        } )
    }

    transferToUserWallet(){
        const userWallet = document.querySelector("#transferToWallet").value;
        const amount = document.querySelector("#transferAmount").value;
        transferTo(userWallet, amount , response =>{
            console.log(response)
         
        } )
    }

    transferFromUserToUserWallet(){
        const senderWalletId = document.querySelector("#senderUserWalletId").value;
        const senderWalletSecret = document.querySelector("#senderUserWalletSecret").value;
        const recipientWallet = document.querySelector("#recipientUserWallet").value;
        const amount = document.querySelector("#senderTransferAmount").value;
        transfer(senderWalletSecret,senderWalletId,recipientWallet, amount , response =>{
            console.log(response)
         
        } )
    }

    addFundsToZudexo(){
        const fund = document.querySelector("#fundAmount").value;
        addFunds(fund, response =>{
            console.log("funds added")
        } )
    }

    balanceOfUserWallet(){
        
        const walletSecret = document.querySelector("#walletIdSecret").value;
         
        balanceOf(walletSecret, response =>{
            console.log(response)
            this.setState({balance:response.data})
        } )
    }

    createOrgWallet(){
        const orgMspIdInput = document.querySelector("#orgMspId");
        const orgMspIdVal = orgMspIdInput.value
        console.log()
        if(!isUndefined(orgMspIdVal)){
            createPartnerWallet(orgMspIdVal,response => {
                this.setState({
                    user_wallet : response.id,
                })
            })
        } else {
            var popupMsg = { msg : "Org Msp ID is empty" , detailedMsg: "Org msp id is cannot be empty"}
            displayCustomPopup(popupMsg,"error")
        }
    }

    render() {
        return (
            <div id='content' className='container form-control-lg'>
                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>
                <div className="row">
                    <label>Creat Org Memeber Wallet</label>
                    <div className="col-sm">
                        <input type="text" id="orgMspId"></input>
                        </div>
                        <div className="col-sm">
                        <input type="button" className="btn btn-info"
                     value='Create Org Wallet' onClick={(e) => this.createOrgWallet()} />
                    </div>
                </div>

                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>
                <div className="row">
                    <label>Add Fund To Zudexo</label>
                    <div className="col-sm">
                        <input type="text" id="fundAmount"></input>
                        </div>
                        <div className="col-sm">
                        <input type="button" className="btn btn-info"
                     value='Add Fund' onClick={(e) => this.addFundsToZudexo()} />
                    </div>
                </div>

                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>

              <div className="row">
                <label className="label">Create User Wallet</label>
                <input type="button" className="btn btn-info"
                 value='Create Wallet' onClick={(e) => this.createUserWallet()} />
                 </div>
                 <div className="row">
                    <label>User Wallet Id</label>
                    <div className="col-sm">
                        {this.state.user_wallet}
                    </div>
                </div>
                <div className="row">
                    <label>User Wallet Secret</label>
                    <div className="col-sm">
                        {this.state.user_wallet_secret}
                    </div>
                </div>

                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>

                <div className="row">
                    <label className="label">Read User Wallet</label>
                    <input type="button" className="btn btn-info"
                     value='Read Wallet' onClick={(e) => this.readUserWallet()} />
                </div>
                <div className="row">
                    <label>User Wallet Balance</label>
                    <div className="col-sm">
                        {this.state.balance}
                    </div>
                </div>


                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>
                <div className="row">
                    <label>Balance Of User Wallet</label>
                    <div className="col-sm">
                        <label>User Wallet ID</label>
                        <input type="text" id="walletId"></input>
                        <label>User Wallet Secret</label>
                        <input type="text" id="walletIdSecret"></input>
                    </div>
                    <div className="col-sm">
                        <input type="button" className="btn btn-info"
                     value='Get Balance' onClick={(e) => this.balanceOfUserWallet()} />
                    </div>
                    <div className="row">
                    <label>User Wallet Balance</label>
                    <div className="col-sm">
                        {this.state.balance}
                    </div>
                </div>
                </div>

                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>
                <div className="row">
                    <label>Transfer To</label>
                    <div className="col-sm">
                        <label>User Wallet ID</label>
                        <input type="text" id="transferToWallet"></input>
                    </div>
                    <div className="col-sm">
                        <label>Amount</label>
                        <input type="text" id="transferAmount"></input>
                    </div>
                    </div>
                    <div className="row">
                    <div className="col-sm">
                        <input type="button" className="btn btn-info"
                     value='Transfer To' onClick={(e) => this.transferToUserWallet()} />
                    </div>
 
                </div>

                <hr  style={{
                    color: '#000000',
                    backgroundColor: '#000000',
                    height: 1,
                    borderColor : '#000000'
                }}/>
                <div className="row">
                    <label>Sell/Buy Transfer </label>
                    <div className="col-sm">
                        <label>Sender User Wallet ID</label>
                        <input type="text" id="senderUserWalletId"></input>
                    </div>
                    <div className="col-sm">
                        <label>Sender User Wallet Secret</label>
                        <input type="text" id="senderUserWalletSecret"></input>
                    </div>
                    <div className="col-sm">
                        <label>Recipient User Wallet ID</label>
                        <input type="text" id="recipientUserWallet"></input>
                    </div>
                    <div className="col-sm">
                        <label>Amount</label>
                        <input type="text" id="senderTransferAmount"></input>
                    </div>
                    </div>
                    <div className="row">
                    <div className="col-sm">
                        <input type="button" className="btn btn-info"
                     value='Transfer' onClick={(e) => this.transferFromUserToUserWallet()} />
                    </div>
 
                </div>

            </div>
        );
    }
}

export default WalletComponent;