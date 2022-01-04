import React, { Component } from 'react'
import { isUndefined, displayCustomPopup } from './CustomUtilities'
import { createWallet, readWallet, createPartnerWallet, balanceOf, addFunds, transferTo, transfer } from './WalletService'
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import ZudexoPageComponent from "./ZudexoPageComponent"
import UserViewPageComponenet from "./UserViewPageComponenet"
import ZiggyPageComponenet from "./ZiggyPageComponenet"


class TabViewWalletComponent extends Component {


    render() {

        return (
            <div className="tabs">
                <Tabs>
                    <TabList>
                        <Tab>Zudexo Admin</Tab>
                        <Tab>User</Tab>
                        <Tab>Ziggy Portal</Tab>
                    </TabList>

                    <TabPanel>
                        <h2>Zudexo Control Panel</h2>
                        <ZudexoPageComponent></ZudexoPageComponent>
                    </TabPanel>
                    <TabPanel>
                        <h2>User Account Page</h2>
                        <UserViewPageComponenet></UserViewPageComponenet>
                    </TabPanel>
                    <TabPanel>
                        <h2>Ziggy Food Online Portal</h2>
                        <ZiggyPageComponenet></ZiggyPageComponenet>
                    </TabPanel>
                </Tabs>
            </div>
        );
    }
}

export default TabViewWalletComponent;